import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import gen.*;
import java.util.HashSet;
import java.util.Set;
public class SemanticAnalyzer extends SPLBaseVisitor<Void> {
    private Set<String> declaredVariables = new HashSet<>();
    private Set<String> initializedVariables = new HashSet<>();
    @Override
    public Void visitVarDecl(SPLParser.VarDeclContext ctx) {
        // Überprüfen der Variablendeklaration
        String identifier = ctx.IDENTIFIER().getText();
        if (isVariableDeclared(identifier)) {
            // Fehler: Variable bereits deklariert
            System.err.println("Fehler: Variable bereits deklariert - " + identifier);
        } else {
            // Variable in den Kontext einfügen
            addVariableToContext(identifier);
        }

        // Überprüfen des optionalen Zuweisungsausdrucks
        if (ctx.expression() != null) {
            visitExpression(ctx.expression());
        }

        return null;
    }

    @Override
    public Void visitExprStmt(SPLParser.ExprStmtContext ctx) {
        // Überprüfen des Ausdrucks in der Ausdrucksanweisung
        visitExpression(ctx.expression());
        return null;
    }

    private boolean isVariableDeclared(String identifier) {
        // Implementiere die Logik zur Überprüfung, ob die Variable bereits deklariert wurde
        // Rückgabe true, wenn die Variable bereits deklariert ist, sonst false
        return declaredVariables.contains(identifier);
    }

    private void addVariableToContext(String identifier) {
        declaredVariables.add(identifier);
    }

    @Override
    public Void visitTerm(SPLParser.TermContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i += 2) {
            ParseTree factor = ctx.getChild(i);
            visitFactor((SPLParser.FactorContext) factor);

            if (i + 1 < ctx.getChildCount()) {
                String operator = ctx.getChild(i + 1).getText();
                ParseTree nextFactor = ctx.getChild(i + 2);

                if (!isNumeric(factor) || !isNumeric(nextFactor)) {
                    // Fehler: Arithmetische Operatoren dürfen nur auf Zahlen angewendet werden
                    System.err.println("Fehler: Arithmetische Operatoren dürfen nur auf Zahlen angewendet werden");
                }
            }
        }
        return null;
    }



    private boolean isNumeric(ParseTree tree) {
        // Überprüfen, ob der Ausdruck eine gültige Zahl ist
        String expression = tree.getText();
        try {
            Double.parseDouble(expression);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Void visitIfStmt(SPLParser.IfStmtContext ctx) {
        // Überprüfen der If-Bedingung
        SPLParser.ExpressionContext condition = ctx.expression();
        if (!isBooleanLiteral(condition)) {
            System.err.println("Fehler: Ungültige If-Bedingung - Nur true oder false erlaubt");
        }

        // Überprüfen des Statement-Blocks
        visitStatement(ctx.statement(0));

        if (ctx.ELSE() != null) {
            // Überprüfen des Else-Blocks
            visitStatement(ctx.statement(1));
        }

        return null;
    }

    @Override
    public Void visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        // Überprüfen der While-Bedingung
        SPLParser.ExpressionContext condition = ctx.expression();
        if (!isBooleanLiteral(condition)) {
            System.err.println("Fehler: Ungültige While-Bedingung - Nur true oder false erlaubt");
        }

        // Überprüfen des Statement-Blocks
        visitStatement(ctx.statement());

        return null;
    }

    private boolean isBooleanLiteral(SPLParser.ExpressionContext ctx) {
        String expression = ctx.getText();
        return expression.equals("true") || expression.equals("false");
    }





}