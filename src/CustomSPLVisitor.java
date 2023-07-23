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

    // Diese Methode wird aufgerufen, um eine While-Schleife im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        SPLValue conditionValue = visitExpression(ctx.expression());

        // Überprüfe, ob der Ausdruck einen Boolean zurückgibt
        if (!(conditionValue instanceof SPLBooleanValue)) {
            throw new RuntimeException("Boolscher Ausdruck im While-Statement erwartet.");
        }
        while (conditionValue.asBoolean()) {
            visitStatement(ctx.statement());
            // Nachdem die Anweisungen ausgeführt wurden, evaluieren wir erneut den Ausdruck (Bedingung).
            // Dadurch wird überprüft, ob die Schleife weiter ausgeführt werden soll oder nicht.
            conditionValue = visitExpression(ctx.expression());
        }
        return new SPLUndefinedValue();
    }
    // Rufe die visit-Methode für jede Deklaration im Block auf
    @Override
    public SPLValue visitBlock(SPLParser.BlockContext ctx) {
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

    // Diese Methode wird aufgerufen, um eine Zuweisung (Assignment) im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitAssignment(SPLParser.AssignmentContext ctx) {
        if (ctx.IDENTIFIER() != null)
            currentVarName = ctx.IDENTIFIER().getText();
        if (ctx.logic_or() != null) {
            SPLValue rightValue = visitLogic_or(ctx.logic_or());
            // Führe die Zuweisung aus
            symbolTable.put(currentVarName, rightValue);
            // Setze den aktuellen Variablennamen zurück, um eventuelle Konflikte zu vermeiden
            currentVarName = "";
            return rightValue;
        } else {
            // Wenn der Ausdruck auf der rechten Seite der Zuweisung kein logischer Ausdruck ist,
            // rufen wir die Methode "visitAssignment" rekursiv auf, um den Ausdruck weiter auszuwerten.
            return visitAssignment(ctx.assignment());
        }
    }

    // Diese Methode wird aufgerufen, um einen logischen ODER-Ausdruck im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitLogic_or(SPLParser.Logic_orContext ctx) {
        // Wenn es mehr als einen Operanden im logischen ODER-Ausdruck gibt
        if (ctx.logic_and().size() > 1) {
            // Evaluieren des ersten Operanden
            SPLValue result = visitLogic_and(ctx.logic_and(0));
            // Iteriere über die restlichen Operanden im logischen ODER-Ausdruck
            for (int i = 1; i < ctx.logic_and().size(); i++) {
                boolean leftValue = result.asBoolean();
                boolean rightValue = visitLogic_and(ctx.logic_and(i)).asBoolean();
                result = new SPLBooleanValue(leftValue || rightValue);
            }
            return result;
        } else {
            // Wenn es nur einen Operanden im logischen ODER-Ausdruck gibt,
            // rufen wir die Methode "visitLogic_and" auf, um den Operanden auszuwerten und das Ergebnis zurückzugeben.
            return visitLogic_and(ctx.logic_and(0));
        }
    }

    // Diese Methode wird aufgerufen, um einen logischen UND-Ausdruck im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitLogic_and(SPLParser.Logic_andContext ctx) {
        // Diese Methode wird aufgerufen, um einen logischen UND-Ausdruck im SPL-Programm zu analysieren und auszuwerten.
        // Wenn es mehr als einen Operanden im logischen UND-Ausdruck gibt
        if (ctx.equality().size() > 1) {
            // Evaluieren des ersten Operanden
            SPLValue result = visitEquality(ctx.equality(0));
            // Iteriere über die restlichen Operanden im logischen UND-Ausdruck
            for (int i = 1; i < ctx.equality().size(); i++) {
                boolean leftValue = result.asBoolean();
                boolean rightValue = visitEquality(ctx.equality(i)).asBoolean();
                result = new SPLBooleanValue(leftValue && rightValue);
            }
            return result;
        } else {
            // Wenn es nur einen Operanden im logischen UND-Ausdruck gibt,
            // rufen wir die Methode "visitEquality" auf, um den Operanden auszuwerten und das Ergebnis zurückzugeben.
            return visitEquality(ctx.equality(0));
        }
    }

    // Diese Methode wird aufgerufen, um einen Gleichheits- oder Ungleichheitsausdruck im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitEquality(SPLParser.EqualityContext ctx) {
        // Evaluieren des ersten Operanden
        SPLValue result = visitComparison(ctx.comparison(0));
        // Iteriere über die restlichen Vergleichsoperationen im Ausdruck
        for (int i = 1; i < ctx.comparison().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            // Evaluieren des aktuellen Operanden
            SPLValue rightValue = visitComparison(ctx.comparison(i));
            // Überprüfe, ob einer der Operanden null ist
            if (result == null || rightValue == null) {
                throw new RuntimeException("Inkompatible Typen. Operationen mit null nicht erlaubt!");
            }
            // Überprüfe die Typen der Operanden und führe die entsprechende Vergleichsoperation aus
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
            } else if (result.isString() && rightValue.isString()) {
                switch (operator) {
                    case "==":
                        result = new SPLBooleanValue(result.asString().equals(rightValue.asString()));
                        break;
                    case "!=":
                        result = new SPLBooleanValue(!result.asString().equals(rightValue.asString()));
                        break;
                }
            } else {
                throw new RuntimeException("Inkompatible Typen. Operanden müssen beide numerisch, boolean oder string und initialisiert sein.");
            }
        }
        return result;
    }

    // Diese Methode wird aufgerufen, um einen Vergleichsausdruck im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitComparison(SPLParser.ComparisonContext ctx) {
        // Evaluieren des ersten Operanden
        SPLValue result = visitTerm(ctx.term(0));
        // Iteriere über die restlichen Operanden
        for (int i = 1; i < ctx.term().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            // Evaluieren des aktuellen Operanden
            SPLValue rightValue = visitTerm(ctx.term(i));
            if (!result.isNumeric() || !rightValue.isNumeric()) {
                throw new RuntimeException("Inkompatible Typen. Operanden müssen beide numerisch sein.");
            }
            // Führe die entsprechende Vergleichsoperation aus und speichere den Wert
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

    // Diese Methode wird aufgerufen, um einen Term-Ausdruck im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitTerm(SPLParser.TermContext ctx) {
        // Evaluieren des ersten Faktors
        SPLValue result = visitFactor(ctx.factor(0));
        // Iteriere über die restlichen Faktoren
        for (int i = 1; i < ctx.factor().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            // Evaluieren des aktuellen Faktors
            SPLValue rightValue = visitFactor(ctx.factor(i));
            if (!result.isNumeric() || !rightValue.isNumeric()) {
                throw new RuntimeException("Inkompatible Typen. Für arithmetische Operationen müssen beide Operanden numerisch sein.");
            }
            // Führe die entsprechende arithmetische Operation aus und speichere den Wert
            switch (operator) {
                case "+":
                    result = result.add(rightValue);
                    break;
                case "-":
                    result = result.subtract(rightValue);
                    break;
                default:
                    throw new RuntimeException("Nicht unterstützter arithmetischer Operator: " + operator);
            }
        }
        return result;
    }

    // Diese Methode wird aufgerufen, um einen Faktor-Ausdruck im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitFactor(SPLParser.FactorContext ctx) {
        // Evaluieren des ersten Faktors
        SPLValue result = visitUnary(ctx.unary(0));
        // Iteriere über die restlichen Faktoren
        for (int i = 1; i < ctx.unary().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            // Evaluieren des aktuellen Faktors
            SPLValue rightValue = visitUnary(ctx.unary(i));
            if (!result.isNumeric() || !rightValue.isNumeric()) {
                throw new RuntimeException("Inkompatible Typen. Für arithmetische Operationen müssen beide Operanden numerisch sein.");
            }
            // Führe die entsprechende arithmetische Operation aus und speichere den Wert
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

    // Diese Methode wird aufgerufen, um einen Unary-Ausdruck im SPL-Programm zu analysieren und auszuwerten.
    @Override
    public SPLValue visitUnary(SPLParser.UnaryContext ctx) {
        // Wenn der Ausdruck eine logische Negation (NOT) ist
        if (ctx.NOT() != null) {
            SPLValue operandValue = visitUnary(ctx.unary());
            // Führe die logische Negation aus und speichere den Wert
            return new SPLBooleanValue(!operandValue.asBoolean());
        }
        // Wenn der Ausdruck eine Negation (Minus) ist
        else if (ctx.MINUS() != null) {
            SPLValue operandValue = visitUnary(ctx.unary());
            // Führe die Negation aus und speichere den Wert
            return operandValue.negate();
        }
        else {
            return visitPrimary(ctx.primary());
        }
    }


    // Überprüfe welches Terminal-Symbol vorliegt und gibt den entsprechenden Wert zurück
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