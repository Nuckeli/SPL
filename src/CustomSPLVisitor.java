import gen.SPLBaseVisitor;
import gen.SPLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.HashMap;
import java.util.Map;
public class CustomSPLVisitor extends SPLBaseVisitor<SPLValue> {
    // Hashmap
    private final Map<String, SPLValue> symbolTable;
    private String currentVarName;

    public CustomSPLVisitor() {
        symbolTable = new HashMap<>();
        currentVarName = null;
    }
    // Diese Methode wird aufgerufen, um das gesamte SPL-Programm zu besuchen und auszuwerten.
    @Override
    public SPLValue visitProgram(SPLParser.ProgramContext ctx) {
        // Schleife über alle Deklarationen im Programm.
        for (SPLParser.DeclarationContext declarationCtx : ctx.declaration()) {
            // Für jede Deklaration rufen wir die entsprechende "visit"-Methode auf, um die Deklaration zu verarbeiten und auszuwerten.
            visit(declarationCtx);
        }
        return null;
    }
    // Diese Methode wird aufgerufen, um eine Deklaration im SPL-Programm zu besuchen und auszuwerten.
    @Override
    public SPLValue visitDeclaration(SPLParser.DeclarationContext ctx) {
        // Überprüfe, ob es sich um eine Variable-Deklaration handelt.
        if (ctx.varDecl() != null) {
            return visitVarDecl(ctx.varDecl());
        }
        // Überprüfe, ob es sich um eine Anweisung handelt.
        else if (ctx.statement() != null) {
            return visitStatement(ctx.statement());
        }
        return null;
    }
    // Diese Methode wird aufgerufen, um eine Variable-Deklaration im SPL-Programm zu besuchen und auszuwerten.
    @Override
    public SPLValue visitVarDecl(SPLParser.VarDeclContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        currentVarName = varName;
        // Initialisiere den Variablenwert als null
        SPLValue varValue = null;
        // Überprüfe, ob die Variable bereits in der Symboltabelle vorhanden ist.
        if (symbolTable.containsKey(varName)) {
            // Falls die Variable bereits deklariert wurde, wird ein Fehler geworfen, da Neudeklarationen nicht erlaubt sind
            throw new RuntimeException("Variable bereits deklariert! Keine Neudeklaration erlaubt!");
        }
        // Überprüfe, ob es eine Zuweisung eines Wertes an die Variable gibt (Variableninitialisierung).
        if (ctx.expression() != null) {
            varValue = visit(ctx.expression());
        }
        symbolTable.put(varName, varValue);

        return varValue;
    }
    // Überprüfe um welche Art von Statement es sich handelt
    @Override
    public SPLValue visitStatement(SPLParser.StatementContext ctx) {
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
        return visitExpression(ctx.expression());
    }

    // Diese Methode wird aufgerufen, um eine If-Anweisung im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitIfStmt(SPLParser.IfStmtContext ctx) {
        SPLValue conditionValue = visitExpression(ctx.expression());
        // Überprüfe, ob der Ausdruck einen Boolean zurückgibt
        if (!(conditionValue instanceof SPLBooleanValue)) {
            throw new RuntimeException("Boolscher Ausdruck im If-Statement erwartet.");
        }
        // Wenn die Bedingung (Ausdruck) "true" ergibt, wird der erste Anweisungsblock (then-Block) ausgeführt.
        if (conditionValue.asBoolean()) {
            return visitStatement(ctx.statement(0));
        } else if (ctx.statement().size() > 1) {
            // Wenn die Bedingung "false" ergibt und ein zweiter Anweisungsblock (else-Block) vorhanden ist, wird dieser ausgeführt.
            return visitStatement(ctx.statement(1));
        }
        return new SPLUndefinedValue();
    }

    @Override
    public SPLValue visitPrintStmt(SPLParser.PrintStmtContext ctx) {
        SPLValue value = visitExpression(ctx.expression());
        System.out.println(value);
        return value;
    }

    @Override
    public SPLValue visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        SPLValue conditionValue = visitExpression(ctx.expression());
        // Überprüfe, ob der Ausdruck einen Boolean zurückgibt
        if (!(conditionValue instanceof SPLBooleanValue)) {
            throw new RuntimeException("Boolscher Ausdruck im If-Statement erwartet.");
        }
        while (conditionValue.asBoolean()) {
            visitStatement(ctx.statement());
            conditionValue = visitExpression(ctx.expression());
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
        if (ctx.assignment() != null) {
            return visitAssignment(ctx.assignment());
        } else {
            throw new RuntimeException("Invalid expression.");
        }
    }

    @Override
    public SPLValue visitAssignment(SPLParser.AssignmentContext ctx) {
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
        if (ctx.logic_and().size() > 1) {
            SPLValue result = visitLogic_and(ctx.logic_and(0));
            for (int i = 1; i < ctx.logic_and().size(); i++) {
                boolean leftValue = result.asBoolean();
                boolean rightValue = visitLogic_and(ctx.logic_and(i)).asBoolean();
                // Führe logisches "ODER" aus und speicher den Wert
                result = new SPLBooleanValue(leftValue || rightValue);
            }
            return result;
        } else {
            return visitLogic_and(ctx.logic_and(0));
        }
    }

    @Override
    public SPLValue visitLogic_and(SPLParser.Logic_andContext ctx) {
        if (ctx.equality().size() > 1) {
            SPLValue result = visitEquality(ctx.equality(0));
            for (int i = 1; i < ctx.equality().size(); i++) {
                boolean leftValue = result.asBoolean();
                boolean rightValue = visitEquality(ctx.equality(i)).asBoolean();
                // Führe logisches "UND" aus und speicher den Wert
                result = new SPLBooleanValue(leftValue && rightValue);
            }
            return result;
        } else {
            return visitEquality(ctx.equality(0));
        }
    }

    @Override
    public SPLValue visitEquality(SPLParser.EqualityContext ctx) {
        SPLValue result = visitComparison(ctx.comparison(0));
        for (int i = 1; i < ctx.comparison().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            SPLValue rightValue = visitComparison(ctx.comparison(i));
            if(result == null || rightValue == null)
                throw new RuntimeException("Inkompatible Typen. Operationen mit null nicht erlaubt!");
            // Überprüfe Vergleichsoperator
            if (result.isNumeric() && rightValue.isNumeric()) {
                switch (operator) {
                    case "==":
                        result = new SPLBooleanValue(result.asDouble() == rightValue.asDouble());
                        break;
                    case "!=":
                        result = new SPLBooleanValue(result.asDouble() != rightValue.asDouble());
                        break;
                }
            } else if (result.isBoolean() && rightValue.isBoolean()) {
                switch (operator) {
                    case "==":
                        result = new SPLBooleanValue(result.asBoolean() == rightValue.asBoolean());
                        break;
                    case "!=":
                        result = new SPLBooleanValue(result.asBoolean() != rightValue.asBoolean());
                        break;
                }
            } else if (result.isString() && rightValue.isString()){
                switch (operator) {
                    case "==":
                        result = new SPLBooleanValue(result.asString().equals(rightValue.asString()));
                        break;
                    case "!=":
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
        return result;
    }

    @Override
    public SPLValue visitUnary(SPLParser.UnaryContext ctx) {
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
        if (ctx.TRUE() != null) {
            return new SPLBooleanValue(true);
        } else if (ctx.FALSE() != null) {
            return new SPLBooleanValue(false);
        } else if (ctx.NUMBER() != null) {
            double numberValue = Double.parseDouble(ctx.NUMBER().getText());
            return new SPLNumberValue(numberValue);
        } else if (ctx.STRING() != null) {
            // Anführungszeichen aus dem String entfernen
            String stringValue = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
            return new SPLStringValue(stringValue);
        } else if (ctx.expression() != null) {
            return visitExpression(ctx.expression());
        } else if (ctx.IDENTIFIER() != null) {
            String varName = ctx.IDENTIFIER().getText();
            if (symbolTable.containsKey(varName)) {
                return symbolTable.get(varName);
            } else {
                // Variable nicht gefunden gebe undefindedvalue zurück
                return new SPLUndefinedValue();
            }
        } else {
            // Gebe undefinedvalue zurück, falls kein passender primary gefunden wurde
            return new SPLUndefinedValue();
        }
    }
}