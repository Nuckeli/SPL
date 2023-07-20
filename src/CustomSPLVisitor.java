import gen.SPLBaseVisitor;
import gen.SPLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.HashMap;
import java.util.Map;
public class CustomSPLVisitor extends SPLBaseVisitor<SPLValue> {

    private final Map<String, SPLValue> symbolTable;

    public CustomSPLVisitor() {
        symbolTable = new HashMap<>();
    }
    @Override
    public SPLValue visitProgram(SPLParser.ProgramContext ctx) {
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
        String varName = ctx.IDENTIFIER().getText();
        SPLValue varValue = null;
        if (ctx.expression() != null) {
            varValue = visit(ctx.expression()); // Wenn eine Zuweisung vorhanden ist, werte den Ausdruck aus
        }
        symbolTable.put(varName, new SPLUndefinedValue());
        printContextInfo(ctx, "Variable Declaration");
        return super.visitVarDecl(ctx);
    }

    @Override
    public SPLValue visitStatement(SPLParser.StatementContext ctx) {
        printContextInfo(ctx, "Statement");
        return visit(ctx);
    }

    @Override
    public SPLValue visitExprStmt(SPLParser.ExprStmtContext ctx) {
        printContextInfo(ctx, "Expression Statement");
        return visit(ctx.expression()); // Rufe die visit-Methode auf, um den Wert des Ausdrucks zu erhalten
    }

    @Override
    public SPLValue visitIfStmt(SPLParser.IfStmtContext ctx) {
        printContextInfo(ctx, "If Statement");
        return super.visitIfStmt(ctx);
    }

    @Override
    public SPLValue visitPrintStmt(SPLParser.PrintStmtContext ctx) {
        printContextInfo(ctx, "Print Statement");
        return super.visitPrintStmt(ctx);
    }

    @Override
    public SPLValue visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        printContextInfo(ctx, "While Statement");
        return super.visitWhileStmt(ctx);
    }

    @Override
    public SPLValue visitBlock(SPLParser.BlockContext ctx) {
        printContextInfo(ctx, "Block");
        return super.visitBlock(ctx);
    }

    @Override
    public SPLValue visitExpression(SPLParser.ExpressionContext ctx) {
        printContextInfo(ctx, "Expression");
        return super.visitExpression(ctx);
    }

    @Override
    public SPLValue visitAssignment(SPLParser.AssignmentContext ctx) {
        printContextInfo(ctx, "Assignment");
        return super.visitAssignment(ctx);
    }

    @Override
    public SPLValue visitLogic_or(SPLParser.Logic_orContext ctx) {
        printContextInfo(ctx, "Logic_or");
        return super.visitLogic_or(ctx);
    }

    @Override
    public SPLValue visitLogic_and(SPLParser.Logic_andContext ctx) {
        printContextInfo(ctx, "Logic_and");
        return super.visitLogic_and(ctx);
    }

    @Override
    public SPLValue visitEquality(SPLParser.EqualityContext ctx) {
        printContextInfo(ctx, "Equality");
        return super.visitEquality(ctx);
    }

    @Override
    public SPLValue visitComparison(SPLParser.ComparisonContext ctx) {
        printContextInfo(ctx, "Comparison");
        return super.visitComparison(ctx);
    }

    @Override
    public SPLValue visitTerm(SPLParser.TermContext ctx) {
        printContextInfo(ctx, "Term");
        return super.visitTerm(ctx);
    }

    @Override
    public SPLValue visitFactor(SPLParser.FactorContext ctx) {
        printContextInfo(ctx, "Factor");
        return super.visitFactor(ctx);
    }

    @Override
    public SPLValue visitUnary(SPLParser.UnaryContext ctx) {
        printContextInfo(ctx, "Unary");
        return super.visitUnary(ctx);
    }

    @Override
    public SPLValue visitPrimary(SPLParser.PrimaryContext ctx) {
        printContextInfo(ctx, "Primary");
        System.out.println(ctx.NUMBER());
        System.out.println(ctx.STRING());
        return super.visitPrimary(ctx);
    }

    // Helper method to print node information
    private void printContextInfo(ParserRuleContext ctx, String nodeName) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        System.out.println("Visiting " + nodeName + " at line " + line + ", column " + column);
        System.out.println(symbolTable);
    }

    private SPLValue evaluateStatement(SPLParser.StatementContext ctx) {
        return visit(ctx); // Rufe die ursprüngliche visit-Methode auf
    }

    private SPLValue evaluateExprStmt(SPLParser.ExprStmtContext ctx) {
        return evaluateExpression(ctx.expression());
    }

    private SPLValue evaluateIfStmt(SPLParser.IfStmtContext ctx) {
        SPLValue conditionValue = evaluateExpression(ctx.expression());
        // Überprüfe, ob der Ausdruck einen Boolean zurückgibt
        if (!(conditionValue instanceof SPLBooleanValue)) {
            throw new RuntimeException("Boolean condition expected in if statement.");
        }

        if (conditionValue.asBoolean()) {
            return evaluateStatement(ctx.statement(0));
        } else if (ctx.statement().size() > 1) {
            return evaluateStatement(ctx.statement(1));
        }
        return new SPLUndefinedValue();
    }


    private SPLValue evaluatePrintStmt(SPLParser.PrintStmtContext ctx) {
        SPLValue value = evaluateExpression(ctx.expression());
        System.out.println(value); // Gib den Wert auf der Konsole aus
        return value; // Gib den Wert zurück, falls es später verwendet werden soll
    }

    private SPLValue evaluateWhileStmt(SPLParser.WhileStmtContext ctx) {
        SPLValue conditionValue = evaluateExpression(ctx.expression());

        // Überprüfe, ob der Ausdruck einen Boolean zurückgibt
        if (!(conditionValue instanceof SPLBooleanValue)) {
            throw new RuntimeException("Boolean condition expected in if statement.");
        }

        while (conditionValue.asBoolean()) {
            evaluateStatement(ctx.statement());
            conditionValue = evaluateExpression(ctx.expression());
        }
        return new SPLUndefinedValue();
    }

    private SPLValue evaluateBlock(SPLParser.BlockContext ctx) {
        return visit(ctx); // Rufe die ursprüngliche visit-Methode auf
    }

    private SPLValue evaluateExpression(SPLParser.ExpressionContext ctx) {
        return evaluateAssignment(ctx.assignment());
    }

    private SPLValue evaluateAssignment(SPLParser.AssignmentContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        SPLValue rightValue = evaluateLogicOr(ctx.logic_or());
        symbolTable.put(varName, rightValue);
        return rightValue;
    }

    // Helper method to evaluate the logic or value
    private SPLValue evaluateLogicOr(SPLParser.Logic_orContext ctx) {
        SPLValue result = evaluateLogicAnd(ctx.logic_and(0));
        for (int i = 1; i < ctx.logic_and().size(); i++) {
            boolean leftValue = result.asBoolean();
            boolean rightValue = evaluateLogicAnd(ctx.logic_and(i)).asBoolean();
            // Perform logical OR and store the result
            result = new SPLBooleanValue(leftValue || rightValue);
        }
        return result;
    }

    // Helper method to evaluate the logic and value
    private SPLValue evaluateLogicAnd(SPLParser.Logic_andContext ctx) {
        SPLValue result = evaluateEquality(ctx.equality(0));
        for (int i = 1; i < ctx.equality().size(); i++) {
            boolean leftValue = result.asBoolean();
            boolean rightValue = evaluateEquality(ctx.equality(i)).asBoolean();
            // Perform logical AND and store the result
            result = new SPLBooleanValue(leftValue && rightValue);
        }
        return result;
    }

    // Helper method to evaluate the equality value
    private SPLValue evaluateEquality(SPLParser.EqualityContext ctx) {
        SPLValue result = evaluateComparison(ctx.comparison(0));
        for (int i = 1; i < ctx.comparison().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = evaluateComparison(ctx.comparison(i));
            switch (operator) {
                case "==":
                    // Perform equality comparison and store the result
                    result = new SPLBooleanValue(result.equals(rightValue));
                    break;
                case "!=":
                    // Perform inequality comparison and store the result
                    result = new SPLBooleanValue(!result.equals(rightValue));
                    break;
            }
        }
        return result;
    }

    private SPLValue evaluateComparison(SPLParser.ComparisonContext ctx) {
        SPLValue result = evaluateTerm(ctx.term(0));
        for (int i = 1; i < ctx.term().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = evaluateTerm(ctx.term(i));
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

    private SPLValue evaluateTerm(SPLParser.TermContext ctx) {
        SPLValue result = evaluateFactor(ctx.factor(0));
        for (int i = 1; i < ctx.factor().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = evaluateFactor(ctx.factor(i));

            // Überprüfe, ob beide Operanden numerisch sind
            if (!result.isNumeric() || !rightValue.isNumeric()) {
                throw new RuntimeException("Operands must be numeric for arithmetic operations!");
            }

            switch (operator) {
                case "+":
                    result = result.add(rightValue);
                    break;
                case "-":
                    result = result.subtract(rightValue);
                    break;
                default:
                    throw new RuntimeException("Unsupported arithmetic operator: " + operator);
            }
        }
        return result;
    }

    private SPLValue evaluateFactor(SPLParser.FactorContext ctx) {
        SPLValue result = evaluateUnary(ctx.unary(0));
        for (int i = 1; i < ctx.unary().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = evaluateUnary(ctx.unary(i));

            // Überprüfe, ob beide Operanden numerisch sind
            if (!result.isNumeric() || !rightValue.isNumeric()) {
                throw new RuntimeException("Operands must be numeric for arithmetic operations!");
            }

            switch (operator) {
                case "*":
                    result = result.multiply(rightValue);
                    break;
                case "/":
                    result = result.divide(rightValue);
                    break;
                default:
                    throw new RuntimeException("Unsupported arithmetic operator: " + operator);
            }
        }
        return result;
    }

    private SPLValue evaluateUnary(SPLParser.UnaryContext ctx) {
        if (ctx.NOT() != null) {
            SPLValue operandValue = evaluateUnary(ctx.unary());
            return new SPLBooleanValue(!operandValue.asBoolean());
        } else if (ctx.MINUS() != null) {
            SPLValue operandValue = evaluateUnary(ctx.unary());
            return operandValue.negate();
        } else {
            return evaluatePrimary(ctx.primary());
        }
    }

    // Helper method to evaluate the primary value
    private SPLValue evaluatePrimary(SPLParser.PrimaryContext ctx) {
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
            return evaluateExpression(ctx.expression());
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
}