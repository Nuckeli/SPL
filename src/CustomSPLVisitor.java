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
        printContextInfo(ctx, "Program");
        return super.visitProgram(ctx);
    }

    @Override
    public SPLValue visitDeclaration(SPLParser.DeclarationContext ctx) {
        printContextInfo(ctx, "Declaration");
        return super.visitDeclaration(ctx);
    }

    @Override
    public SPLValue visitVarDecl(SPLParser.VarDeclContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        symbolTable.put(varName, new SPLUndefinedValue());
        printContextInfo(ctx, "Variable Declaration");
        return super.visitVarDecl(ctx);
    }

    @Override
    public SPLValue visitStatement(SPLParser.StatementContext ctx) {
        printContextInfo(ctx, "Statement");
        return super.visitStatement(ctx);
    }

    @Override
    public SPLValue visitExprStmt(SPLParser.ExprStmtContext ctx) {
        printContextInfo(ctx, "Expression Statement");
        return super.visitExprStmt(ctx);
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
        return super.visitPrimary(ctx);
    }
    // Override other visit methods if needed...

    // Helper method to print node information
    private void printContextInfo(ParserRuleContext ctx, String nodeName) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        System.out.println("Visiting " + nodeName + " at line " + line + ", column " + column);
    }
    /*
    private double evaluateExpression(SPLParser.Logic_orContext exprCtx) {
        // Add your expression evaluation logic here
        // You'll need to traverse the expression subtree and compute the final value
        return 0.0; // Placeholder value
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

    // ...

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
    */
}