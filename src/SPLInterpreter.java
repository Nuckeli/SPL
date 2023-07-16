import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
import java.util.Map;
import gen.*;

public class SPLInterpreter extends SPLBaseVisitor<Void> {

    private Map<String, Integer> variables;

    public SPLInterpreter() {
        variables = new HashMap<>();
    }

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

    @Override
    public Void visitPrintStmt(SPLParser.PrintStmtContext ctx) {
        visitExpression(ctx.expression());
        int value = evaluateExpression(ctx.expression());
        System.out.println(value);
        return null;
    }

    @Override
    public Void visitAssignment(SPLParser.AssignmentContext ctx) {
        String variableName = ctx.IDENTIFIER().getText();
        if (!variables.containsKey(variableName)) {
            throw new RuntimeException("Variable " + variableName + " is not declared.");
        }
        visitExpression(ctx.expression());
        int value = evaluateExpression(ctx.expression());
        variables.put(variableName, value);
        return null;
    }

    @Override
    public Void visitIfStmt(SPLParser.IfStmtContext ctx) {
        visitExpression(ctx.expression());
        int conditionValue = evaluateExpression(ctx.expression());
        if (conditionValue != 0) {
            visitBlock(ctx.block(0)); // Execute the if block
        } else if (ctx.block(1) != null) {
            visitBlock(ctx.block(1)); // Execute the else block (if present)
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        while (true) {
            visitExpression(ctx.expression());
            int conditionValue = evaluateExpression(ctx.expression());
            if (conditionValue == 0) {
                break; // Exit the loop if the condition is false
            }
            visitBlock(ctx.block());
        }
        return null;
    }

    @Override
    public Void visitExpression(SPLParser.ExpressionContext ctx) {
        visitAssignment(ctx.assignment());
        return null;
    }

    private int evaluateExpression(SPLParser.ExpressionContext ctx) {
        return evaluateAssignment(ctx.assignment());
    }

    private int evaluateAssignment(SPLParser.AssignmentContext assignment) {
        if (assignment.logic_or() != null) {
            return evaluateLogicOr(assignment.logic_or());
        }
        // Handle other cases if needed
        return 0;
    }

    private int evaluateLogicOr(SPLParser.Logic_orContext logicOr) {
        if (logicOr.logic_and().size() == 1) {
            return evaluateLogicAnd(logicOr.logic_and(0));
        }
        // Handle other cases if needed
        return 0;
    }

    // Implement evaluation methods for other expressions and operators

    @Override
    public Void visitBlock(SPLParser.BlockContext ctx) {
        for (SPLParser.StatementContext statement : ctx.statement()) {
            visitStatement(statement);
        }
        return null;
    }

    @Override
    public Void visitStatement(SPLParser.StatementContext ctx) {
        if (ctx.varDecl() != null) {
            visitVarDecl(ctx.varDecl());
        } else if (ctx.assignment() != null) {
            visitAssignment(ctx.assignment());
        } else if (ctx.ifStmt() != null) {
            visitIfStmt(ctx.ifStmt());
        } else if (ctx.whileStmt() != null) {
            visitWhileStmt(ctx.whileStmt());
        } else if (ctx.printStmt() != null) {
            visitPrintStmt(ctx.printStmt());
        }
        return null;
    }

    // Implement other visitor methods as needed

    public void interpret(ParseTree parseTree) {
        visit(parseTree);
    }
}