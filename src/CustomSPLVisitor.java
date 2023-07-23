import gen.SPLBaseVisitor;
import gen.SPLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.HashMap;
import java.util.Map;
public class CustomSPLVisitor extends SPLBaseVisitor<SPLValue> {

    private final Map<String, SPLValue> symbolTable;
    private String currentVarName;

    public CustomSPLVisitor() {
        symbolTable = new HashMap<>();
        currentVarName = null;
    }
    @Override
    public SPLValue visitProgram(SPLParser.ProgramContext ctx) {
        printContextInfo(ctx, "Program");
        for (SPLParser.DeclarationContext declarationCtx : ctx.declaration()) {
            visit(declarationCtx);
        }
        return null; // oder ein spezifischer Wert, wenn erforderlich
    }

    @Override
    public SPLValue visitDeclaration(SPLParser.DeclarationContext ctx) {
        printContextInfo(ctx, "Declaration");
        if (ctx.varDecl() != null) {
            return visitVarDecl(ctx.varDecl());
        } else if (ctx.statement() != null) {
            return visitStatement(ctx.statement());
        }
        return null;
    }

    @Override
    public SPLValue visitVarDecl(SPLParser.VarDeclContext ctx) {
        printContextInfo(ctx, "VarDeclaration");
        String varName = ctx.IDENTIFIER().getText();

        currentVarName = varName;
        SPLValue varValue = null; // Standardmäßig ist der Variablenwert null
        if (symbolTable.containsKey(varName)) {
            throw new RuntimeException("Variable bereits deklariert! Keine Neudeklaration erlaubt!");
        }
        if (ctx.expression() != null) {
            varValue = visit(ctx.expression()); // Wenn eine Zuweisung vorhanden ist, werte den Ausdruck aus
        }
        System.out.println(varName);


        // Füge den Variablennamen und den Wert in die Symboltabelle ein
        System.out.println(varName);
        symbolTable.put(varName, varValue);


        return varValue;
    }

    @Override
    public SPLValue visitStatement(SPLParser.StatementContext ctx) {
        printContextInfo(ctx, "Statement");
        if (ctx.exprStmt() != null) {
            return visitExprStmt(ctx.exprStmt());
        } else if (ctx.ifStmt() != null) {
            return visitIfStmt(ctx.ifStmt());
        } else if (ctx.printStmt() != null) {
            return visitPrintStmt(ctx.printStmt());
        } else if (ctx.whileStmt() != null) {
            return visitWhileStmt(ctx.whileStmt());
        } else if (ctx.block() != null) {
            return visitBlock(ctx.block());
        } else {
            throw new RuntimeException("Invalid statement.");
        }
    }

    @Override
    public SPLValue visitExprStmt(SPLParser.ExprStmtContext ctx) {
        printContextInfo(ctx, "Expression Statement");
        return visitExpression(ctx.expression()); // Rufe die visit-Methode auf, um den Wert des Ausdrucks zu erhalten
    }

    @Override
    public SPLValue visitIfStmt(SPLParser.IfStmtContext ctx) {
        printContextInfo(ctx, "If Statement");
        SPLValue conditionValue = visitExpression(ctx.expression());
        System.out.println("\n\n\nCONDITIONVALUE: "+ conditionValue);
        // Überprüfe, ob der Ausdruck einen Boolean zurückgibt
        if (!(conditionValue instanceof SPLBooleanValue)) {
            throw new RuntimeException("Boolscher Ausdruck im If-Statement erwartet.");
        }

        if (conditionValue.asBoolean()) {
            return visitStatement(ctx.statement(0));
        } else if (ctx.statement().size() > 1) {
            return visitStatement(ctx.statement(1));
        }
        return new SPLUndefinedValue();    }

    @Override
    public SPLValue visitPrintStmt(SPLParser.PrintStmtContext ctx) {
        printContextInfo(ctx, "Print Statement");
        SPLValue value = visitExpression(ctx.expression());
        System.out.println(value); // Gib den Wert auf der Konsole aus
        return value; // Gib den Wert zurück, falls es später verwendet werden soll

    }

    @Override
    public SPLValue visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        printContextInfo(ctx, "While Statement");
        SPLValue conditionValue = visitExpression(ctx.expression());

        // Überprüfe, ob der Ausdruck einen Boolean zurückgibt
        if (!(conditionValue instanceof SPLBooleanValue)) {
            throw new RuntimeException("Boolscher Ausdruck im If-Statement erwartet.");
        }
        int b = 20;
        while (conditionValue.asBoolean()) {
            visitStatement(ctx.statement());
            conditionValue = visitExpression(ctx.expression());
            //if (b == 0) {throw new RuntimeException("zu oft iteriert"); }
            //b--;

        }
        return new SPLUndefinedValue();
    }

    @Override
    public SPLValue visitBlock(SPLParser.BlockContext ctx) {
        // Rufe die visit-Methode für jede Deklaration im Block auf
        for (SPLParser.DeclarationContext declarationContext : ctx.declaration()) {
            visitDeclaration(declarationContext);
        }
        return new SPLUndefinedValue();
    }

    @Override
    public SPLValue visitExpression(SPLParser.ExpressionContext ctx) {
        printContextInfo(ctx, "Expression");
        if (ctx.assignment() != null) {
            return visitAssignment(ctx.assignment());
        } else {
            throw new RuntimeException("Invalid expression.");
        }
    }

    @Override
    public SPLValue visitAssignment(SPLParser.AssignmentContext ctx) {
        printContextInfo(ctx, "Assignment");

        if (ctx.IDENTIFIER() != null)
            currentVarName = ctx.IDENTIFIER().getText();
        if (ctx.logic_or() != null) {
            SPLValue rightValue = visitLogic_or(ctx.logic_or());
            symbolTable.put(currentVarName, rightValue);
            currentVarName = "";
            return rightValue;
        } else {
            return visitAssignment(ctx.assignment());
        }
    }

    @Override
    public SPLValue visitLogic_or(SPLParser.Logic_orContext ctx) {
        printContextInfo(ctx, "Logic_or");
        if (ctx.logic_and().size() > 1) {
            SPLValue result = visitLogic_and(ctx.logic_and(0));
            for (int i = 1; i < ctx.logic_and().size(); i++) {
                boolean leftValue = result.asBoolean();
                boolean rightValue = visitLogic_and(ctx.logic_and(i)).asBoolean();
                // Perform logical OR and store the result
                result = new SPLBooleanValue(leftValue || rightValue);
            }
            return result;
        } else {
            return visitLogic_and(ctx.logic_and(0));
        }
    }

    @Override
    public SPLValue visitLogic_and(SPLParser.Logic_andContext ctx) {
        printContextInfo(ctx, "Logic_and");
        if (ctx.equality().size() > 1) {
            SPLValue result = visitEquality(ctx.equality(0));
            for (int i = 1; i < ctx.equality().size(); i++) {
                boolean leftValue = result.asBoolean();
                boolean rightValue = visitEquality(ctx.equality(i)).asBoolean();
                // Perform logical AND and store the result
                result = new SPLBooleanValue(leftValue && rightValue);
            }
            return result;
        } else {
            return visitEquality(ctx.equality(0));
        }
    }

    @Override
    public SPLValue visitEquality(SPLParser.EqualityContext ctx) {
        printContextInfo(ctx, "Equality");
        SPLValue result = visitComparison(ctx.comparison(0));
        for (int i = 1; i < ctx.comparison().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = visitComparison(ctx.comparison(i));
            if (result.isNumeric() && rightValue.isNumeric()) {
                switch (operator) {
                    case "==":
                        // Perform equality comparison and store the result
                        System.out.println("\n\n\n result == rightValue " + result + " " + (result.asDouble() == rightValue.asDouble()) + " " + rightValue);
                        result = new SPLBooleanValue(result.asDouble() == rightValue.asDouble());
                        break;
                    case "!=":
                        // Perform inequality comparison and store the result
                        System.out.println("\n\n\n result != rightValue :" + result + " " + (result.asDouble() != rightValue.asDouble()) + " " + rightValue);
                        result = new SPLBooleanValue(result.asDouble() != rightValue.asDouble());
                        break;
                }
            } else if (result.isBoolean() && rightValue.isBoolean()) {
                switch (operator) {
                    case "==":
                        // Perform equality comparison and store the result
                        System.out.println("\n\n\n result == rightValue " + result + " " + (result.asBoolean() == rightValue.asBoolean()) + " " + rightValue);
                        result = new SPLBooleanValue(result.asBoolean() == rightValue.asBoolean());
                        break;
                    case "!=":
                        // Perform inequality comparison and store the result
                        System.out.println("\n\n\n result != rightValue :" + result + " " + (result.asBoolean() != rightValue.asBoolean()) + " " + rightValue);
                        result = new SPLBooleanValue(result.asBoolean() != rightValue.asBoolean());
                        break;
                }
            } else if (result.isString() && rightValue.isString()){
                switch (operator) {
                    case "==":
                        // Perform equality comparison and store the result
                        System.out.println("\n\n\n result == rightValue " + result + " " + (result.asString().equals(rightValue.asString())) + " " + rightValue);
                        result = new SPLBooleanValue(result.asString().equals(rightValue.asString()));
                        break;
                    case "!=":
                        // Perform inequality comparison and store the result
                        System.out.println("\n\n\n result != rightValue :" + result + " " + !(result.asString().equals(rightValue.asString())) + " " + rightValue);
                        result = new SPLBooleanValue(!(result.asString().equals(rightValue.asString())));
                        break;
                }
            } else
                throw new RuntimeException("Inkompatible Typen. Operanden müssen beide numerisch, boolean oder string und initialisiert sein.");

        }
        return result;
    }

    @Override
    public SPLValue visitComparison(SPLParser.ComparisonContext ctx) {
        printContextInfo(ctx, "Comparison");
        SPLValue result = visitTerm(ctx.term(0));
        for (int i = 1; i < ctx.term().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = visitTerm(ctx.term(i));
            if (!result.isNumeric() || !rightValue.isNumeric()) {
                throw new RuntimeException("Inkompatible Typen. Operanden müssen beide numerisch sein.");
            }
            switch (operator) {
                case ">":
                    result = new SPLBooleanValue(result.asDouble() > rightValue.asDouble());
                    break;
                case ">=":
                    result = new SPLBooleanValue(result.asDouble() >= rightValue.asDouble());
                    break;
                case "<":
                    result = new SPLBooleanValue(result.asDouble() < rightValue.asDouble());
                    break;
                case "<=":
                    result = new SPLBooleanValue(result.asDouble() <= rightValue.asDouble());
                    break;
            }
        }
        return result;
    }

    @Override
    public SPLValue visitTerm(SPLParser.TermContext ctx) {
        printContextInfo(ctx, "Term");
        SPLValue result = visitFactor(ctx.factor(0));
        for (int i = 1; i < ctx.factor().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = visitFactor(ctx.factor(i));
            // Überprüfe, ob beide Operanden numerisch sind
            if (!result.isNumeric() || !rightValue.isNumeric()) {
                throw new RuntimeException("Inkompatible Typen. Für arithmetische Operationen müssen beide Operanden numerisch sein.");
            }

            switch (operator) {
                case "+":
                    result = result.add(rightValue);

                    break;
                case "-":
                    result = result.subtract(rightValue);
                    break;
                default:
                    throw new RuntimeException("Nicht unterstützer arithmetischer Operator: " + operator);
            }
        }
        return result;
    }

    @Override
    public SPLValue visitFactor(SPLParser.FactorContext ctx) {
        printContextInfo(ctx, "Factor");
        SPLValue result = visitUnary(ctx.unary(0));
        for (int i = 1; i < ctx.unary().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = visitUnary(ctx.unary(i));

            // Überprüfe, ob beide Operanden numerisch sind
            if (!result.isNumeric() || !rightValue.isNumeric()) {
                throw new RuntimeException("Inkompatible Typen. Für arithmetische Operationen müssen beide Operanden numerisch sein.");
            }

            switch (operator) {
                case "*":
                    result = result.multiply(rightValue);
                    break;
                case "/":
                    result = result.divide(rightValue);
                    break;
                default:
                    throw new RuntimeException("Nicht unterstützter arithmetischer Operator: " + operator);
            }
        }
        return result;    }

    @Override
    public SPLValue visitUnary(SPLParser.UnaryContext ctx) {
        printContextInfo(ctx, "Unary");
        if (ctx.NOT() != null) {
            SPLValue operandValue = visitUnary(ctx.unary());
            return new SPLBooleanValue(!operandValue.asBoolean());
        } else if (ctx.MINUS() != null) {
            SPLValue operandValue = visitUnary(ctx.unary());
            return operandValue.negate();
        } else {
            return visitPrimary(ctx.primary());
        }
    }

    @Override
    public SPLValue visitPrimary(SPLParser.PrimaryContext ctx) {
        printContextInfo(ctx, "Primary");
        if (ctx.TRUE() != null) {
            return new SPLBooleanValue(true);
        } else if (ctx.FALSE() != null) {
            return new SPLBooleanValue(false);
        } else if (ctx.NUMBER() != null) {
            double numberValue = Double.parseDouble(ctx.NUMBER().getText());
            return new SPLNumberValue(numberValue);
        } else if (ctx.STRING() != null) {
            // Remove the surrounding quotes from the string value
            String stringValue = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
            return new SPLStringValue(stringValue);
        } else if (ctx.expression() != null) {
            return visitExpression(ctx.expression());
        } else if (ctx.IDENTIFIER() != null) {
            String varName = ctx.IDENTIFIER().getText();
            if (symbolTable.containsKey(varName)) {
                return symbolTable.get(varName);
            } else {
                // Variable not found, return an undefined value
                System.out.println("Variable '" + varName + "' not found!");
                return new SPLUndefinedValue();
            }
        } else {
            // Return an undefined value if no matching primary is found
            return new SPLUndefinedValue();
        }
    }

    // Helper method to print node information
    private void printContextInfo(ParserRuleContext ctx, String nodeName) {
        if (ctx != null) {
            int line = ctx.getStart().getLine();
            int column = ctx.getStart().getCharPositionInLine();
            System.out.println("Visiting " + nodeName + " at line " + line + ", column " + column);
            System.out.println("symbolTable: " + symbolTable);
        }
    }



}