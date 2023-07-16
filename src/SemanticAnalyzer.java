import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
import java.util.Map;
import gen.*;


public class SemanticAnalyzer extends SPLBaseVisitor<Void> {

    private Map<String, Integer> variables;

    public SemanticAnalyzer() {
        variables = new HashMap<>();
    }

    // Check if variable is declared more than once
    @Override
    public Void visitVarDecl(SPLParser.VarDeclContext ctx) {
        String variableName = ctx.IDENTIFIER().getText();
        if (variables.containsKey(variableName)) {
            throw new RuntimeException("Variable " + variableName + " already declared.");
        }
        if (ctx.expression() != null) {
            visitExpression(ctx.expression());
            int value = evaluateExpression(ctx.expression());
            variables.put(variableName, value);
        } else {
            variables.put(variableName, null); // Set default value to null
        }
        return null;
    }

    // Check if non-boolean value is used in boolean context
    private void checkBooleanConversion(SPLParser.ExpressionContext ctx) {
        if (ctx.assignment() != null) {
            SPLParser.AssignmentContext assignment = ctx.assignment();
            if (!isBooleanType(assignment)) {
                throw new RuntimeException("Non-boolean value used in a boolean context.");
            }
        }
    }


    // Check if non-numeric value is used in arithmetic context
    private void checkNumericConversion(SPLParser.ExpressionContext ctx) {
        if (ctx.assignment() != null) {
            SPLParser.AssignmentContext assignment = ctx.assignment();
            if (!isNumericType(assignment)) {
                throw new RuntimeException("Non-numeric value used in an arithmetic context.");
            }
        }
    }

    @Override
    public Void visitExpression(SPLParser.ExpressionContext ctx) {
        visitAssignment(ctx.assignment());
        checkBooleanConversion(ctx);
        checkNumericConversion(ctx);
        return null;
    }

    private boolean isBooleanType(SPLParser.AssignmentContext assignment) {
        return isBooleanType(assignment.logic_or());
    }

    private boolean isBooleanType(SPLParser.Logic_orContext logicOr) {
        return isBooleanType(logicOr.logic_and(0));
    }

    private boolean isBooleanType(SPLParser.Logic_andContext logicAnd) {
        return isBooleanType(logicAnd.equality(0));
    }

    private boolean isBooleanType(SPLParser.EqualityContext equality) {
        return isBooleanType(equality.comparison(0));
    }

    private boolean isBooleanType(SPLParser.ComparisonContext comparison) {
        return isBooleanType(comparison.term(0));
    }

    private boolean isBooleanType(SPLParser.TermContext term) {
        return isBooleanType(term.factor(0));
    }

    private boolean isBooleanType(SPLParser.FactorContext factor) {
        return isBooleanType(factor.unary(0));
    }

    private boolean isBooleanType(SPLParser.UnaryContext unary) {
        return unary.NOT() != null || isBooleanType(unary.primary());
    }

    private boolean isBooleanType(SPLParser.PrimaryContext primary) {
        return primary.TRUE() != null || primary.FALSE() != null;
    }

    private boolean isNumericType(SPLParser.AssignmentContext assignment) {
        return isNumericType(assignment.logic_or());
    }

    private boolean isNumericType(SPLParser.Logic_orContext logicOr) {
        return isNumericType(logicOr.logic_and(0));
    }

    private boolean isNumericType(SPLParser.Logic_andContext logicAnd) {
        return isNumericType(logicAnd.equality(0));
    }

    private boolean isNumericType(SPLParser.EqualityContext equality) {
        return isNumericType(equality.comparison(0));
    }

    private boolean isNumericType(SPLParser.ComparisonContext comparison) {
        return isNumericType(comparison.term(0));
    }

    private boolean isNumericType(SPLParser.TermContext term) {
        return isNumericType(term.factor(0));
    }

    private boolean isNumericType(SPLParser.FactorContext factor) {
        return isNumericType(factor.unary(0));
    }

    private boolean isNumericType(SPLParser.UnaryContext unary) {
        return isNumericType(unary.primary());
    }

    private boolean isNumericType(SPLParser.PrimaryContext primary) {
        return primary.NUMBER() != null;
    }

    // Print null if uninitialized variable is used in print statement
    @Override
    public Void visitPrintStmt(SPLParser.PrintStmtContext ctx) {
        visitExpression(ctx.expression());
        if (ctx.expression().assignment() != null) {
            SPLParser.AssignmentContext assignment = ctx.expression().assignment();
            if (containsUninitializedVariable(assignment)) {
                System.out.println("null"); // Print null for uninitialized variables
                return null;
            }
        }
        // Continue with the regular behavior
        return super.visitPrintStmt(ctx);
    }

    private int evaluateExpression(SPLParser.ExpressionContext ctx) {
        return evaluateAssignment(ctx.assignment());
    }

    // Evaluate the value of an assignment
    private int evaluateAssignment(SPLParser.AssignmentContext assignment) {
        if (assignment.logic_or() != null) {
            return evaluateLogicOr(assignment.logic_or());
        }
        // Handle other cases if needed
        return 0;
    }

    // Evaluate the value of a logic OR expression
    private int evaluateLogicOr(SPLParser.Logic_orContext logicOr) {
        if (logicOr.logic_and().size() == 1) {
            return evaluateLogicAnd(logicOr.logic_and(0));
        }
        // Handle other cases if needed
        return 0;
    }

    // Evaluate the value of a logic AND expression
    private int evaluateLogicAnd(SPLParser.Logic_andContext logicAnd) {
        if (logicAnd.equality().size() == 1) {
            return evaluateEquality(logicAnd.equality(0));
        }
        // Handle other cases if needed
        return 0;
    }

    // Evaluate the value of an equality expression
    private int evaluateEquality(SPLParser.EqualityContext equality) {
        if (equality.comparison().size() == 1) {
            return evaluateComparison(equality.comparison(0));
        }
        // Handle other cases if needed
        return 0;
    }

    // Evaluate the value of a comparison expression
    private int evaluateComparison(SPLParser.ComparisonContext comparison) {
        if (comparison.term().size() == 1) {
            return evaluateTerm(comparison.term(0));
        }
        // Handle other cases if needed
        return 0;
    }

    // Evaluate the value of a term expression
    private int evaluateTerm(SPLParser.TermContext term) {
        if (term.factor().size() == 1) {
            return evaluateFactor(term.factor(0));
        }
        // Handle other cases if needed
        return 0;
    }

    // Evaluate the value of a factor expression
    private int evaluateFactor(SPLParser.FactorContext factor) {
        if (factor.unary().size() == 1) {
            return evaluateUnary(factor.unary(0));
        }
        // Handle other cases if needed
        return 0;
    }

    // Evaluate the value of a unary expression
    private int evaluateUnary(SPLParser.UnaryContext unary) {
        if (unary.primary() != null) {
            return evaluatePrimary(unary.primary());
        }
        // Handle other cases if needed
        return 0;
    }

    // Evaluate the value of a primary expression
    private int evaluatePrimary(SPLParser.PrimaryContext primary) {
        if (primary.NUMBER() != null) {
            return Integer.parseInt(primary.NUMBER().getText());
        }
        // Handle other cases if needed
        return 0;
    }


    private boolean containsUninitializedVariable(SPLParser.ExpressionContext expression) {
        return containsUninitializedVariable(expression.assignment());
    }
    // Check if an assignment contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.AssignmentContext assignment) {
        if (assignment.logic_or() != null) {
            return containsUninitializedVariable(assignment.logic_or());
        }
        // Handle other cases if needed
        return false;
    }

    // Check if a logic OR expression contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.Logic_orContext logicOr) {
        for (SPLParser.Logic_andContext logicAnd : logicOr.logic_and()) {
            if (containsUninitializedVariable(logicAnd)) {
                return true;
            }
        }
        return false;
    }

    // Check if a logic AND expression contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.Logic_andContext logicAnd) {
        for (SPLParser.EqualityContext equality : logicAnd.equality()) {
            if (containsUninitializedVariable(equality)) {
                return true;
            }
        }
        return false;
    }

    // Check if an equality expression contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.EqualityContext equality) {
        for (SPLParser.ComparisonContext comparison : equality.comparison()) {
            if (containsUninitializedVariable(comparison)) {
                return true;
            }
        }
        return false;
    }

    // Check if a comparison expression contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.ComparisonContext comparison) {
        for (SPLParser.TermContext term : comparison.term()) {
            if (containsUninitializedVariable(term)) {
                return true;
            }
        }
        return false;
    }

    // Check if a term expression contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.TermContext term) {
        for (SPLParser.FactorContext factor : term.factor()) {
            if (containsUninitializedVariable(factor)) {
                return true;
            }
        }
        return false;
    }

    // Check if a factor expression contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.FactorContext factor) {
        for (SPLParser.UnaryContext unary : factor.unary()) {
            if (containsUninitializedVariable(unary)) {
                return true;
            }
        }
        return false;
    }

    // Check if a unary expression contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.UnaryContext unary) {
        if (unary.primary() != null) {
            return containsUninitializedVariable(unary.primary());
        }
        // Handle other cases if needed
        return false;
    }


    // Check if a primary expression contains uninitialized variables
    private boolean containsUninitializedVariable(SPLParser.PrimaryContext primary) {
        if (primary.IDENTIFIER() != null) {
            String variableName = primary.IDENTIFIER().getText();
            return !variables.containsKey(variableName);
        }
        // Handle other cases if needed
        return false;
    }

    // Check if a primary expression contains a null comparison
    private boolean containsNullComparison(SPLParser.ExpressionContext expression) {
        return containsNullComparison(expression.assignment());
    }
    private boolean containsNullComparison(SPLParser.AssignmentContext assignment) {
        return containsNullComparison(assignment.logic_or());
    }


    private boolean containsNullComparison(SPLParser.Logic_orContext logicOr) {
        for (SPLParser.Logic_andContext logicAnd : logicOr.logic_and()) {
            if (containsNullComparison(logicAnd)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNullComparison(SPLParser.Logic_andContext logicAnd) {
        for (SPLParser.EqualityContext equality : logicAnd.equality()) {
            if (containsNullComparison(equality)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNullComparison(SPLParser.EqualityContext equality) {
        for (SPLParser.ComparisonContext comparison : equality.comparison()) {
            if (containsNullComparison(comparison)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNullComparison(SPLParser.ComparisonContext comparison) {
        for (SPLParser.TermContext term : comparison.term()) {
            if (containsNullComparison(term)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNullComparison(SPLParser.TermContext term) {
        for (SPLParser.FactorContext factor : term.factor()) {
            if (containsNullComparison(factor)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNullComparison(SPLParser.FactorContext factor) {
        for (SPLParser.UnaryContext unary : factor.unary()) {
            if (containsNullComparison(unary)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNullComparison(SPLParser.UnaryContext unary) {
        if (unary.primary() != null) {
            return containsNullComparison(unary.primary());
        }
        // Handle other cases if needed
        return false;
    }

    private boolean containsNullComparison(SPLParser.PrimaryContext primary) {
        if (primary.IDENTIFIER() != null) {
            String variableName = primary.IDENTIFIER().getText();
            return variables.containsKey(variableName) && variables.get(variableName) == null;
        }
        // Handle other cases if needed
        return false;
    }

    // Check if an expression contains uninitialized variables or null comparisons
    private boolean containsErrors(SPLParser.ExpressionContext expression) {
        return containsUninitializedVariable(expression) || containsNullComparison(expression) || containsTypeErrors(expression);
    }

    // Check if an expression contains type errors, such as using boolean values in arithmetic context or using numeric values in boolean context
    private boolean containsTypeErrors(SPLParser.ExpressionContext expression) {
        return containsBooleanConversionError(expression) || containsNumericConversionError(expression);
    }

    // Check if an expression contains boolean conversion errors, such as using non-boolean values in boolean context
    private boolean containsBooleanConversionError(SPLParser.ExpressionContext expression) {
        if (expression.assignment() != null) {
            SPLParser.AssignmentContext assignment = expression.assignment();
            if (!isBooleanType(assignment)) {
                return true;
            }
        }
        return false;
    }

    // Check if an expression contains numeric conversion errors, such as using non-numeric values in arithmetic context
    private boolean containsNumericConversionError(SPLParser.ExpressionContext expression) {
        if (expression.assignment() != null) {
            SPLParser.AssignmentContext assignment = expression.assignment();
            if (!isNumericType(assignment)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Void visitIfStmt(SPLParser.IfStmtContext ctx) {
        visitExpression(ctx.expression());
        if (containsErrors(ctx.expression())) {
            throw new RuntimeException("Invalid usage of uninitialized variables or null comparisons in if condition.");
        }
        // Visit other parts of the if statement if needed
        return null;
    }

    @Override
    public Void visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        visitExpression(ctx.expression());
        if (containsErrors(ctx.expression())) {
            throw new RuntimeException("Invalid usage of uninitialized variables or null comparisons in while condition.");
        }
        // Visit other parts of the while statement if needed
        return null;
    }
}
