import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
import java.util.Map;
import gen.*;

public class SPLInterpreter extends SPLBaseVisitor<Integer> {
    private Map<String, Integer> variables;

    public SPLInterpreter() {
        variables = new HashMap<>();
    }

    public void interpret(ParseTree tree) {
        visit(tree);
    }

    @Override
    public Integer visitVarDecl(SPLParser.VarDeclContext ctx) {
        String variableName = ctx.IDENTIFIER().getText();
        if (variables.containsKey(variableName)) {
            throw new RuntimeException("Variable " + variableName + " already declared.");
        }
        if (ctx.expression() != null) {
            int value = visitExpression(ctx.expression());
            variables.put(variableName, value);
        } else {
            variables.put(variableName, null); // Set default value to null
        }
        return null;
    }

    @Override
    public Integer visitPrintStmt(SPLParser.PrintStmtContext ctx) {
        int value = visitExpression(ctx.expression());
        System.out.println(value);
        return null;
    }

    @Override
    public Integer visitIfStmt(SPLParser.IfStmtContext ctx) {
        int conditionValue = visitExpression(ctx.expression());
        if (conditionValue != 0) {
            visitStatement(ctx.statement(0));
        } else if (ctx.statement().size() > 1) {
            visitStatement(ctx.statement(1));
        }
        return null;
    }

    @Override
    public Integer visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        while (visitExpression(ctx.expression()) != 0) {
            visitStatement(ctx.statement());
        }
        return null;
    }

    @Override
    public Integer visitBlock(SPLParser.BlockContext ctx) {
        for (SPLParser.DeclarationContext declaration : ctx.declaration()) {
            visitDeclaration(declaration);
        }
        return null;
    }

    @Override
    public Integer visitExprStmt(SPLParser.ExprStmtContext ctx) {
        visitExpression(ctx.expression());
        return null;
    }

    @Override
    public Integer visitAssignment(SPLParser.AssignmentContext ctx) {
        String variableName = ctx.IDENTIFIER().getText();
        if (!variables.containsKey(variableName)) {
            throw new RuntimeException("Variable " + variableName + " not declared.");
        }
        int value = visitExpression(ctx.expression());
        variables.put(variableName, value);
        return null;
    }

    @Override
    public Integer visitLogicOr(SPLParser.LogicOrContext ctx) {
        int result = visitLogicAnd(ctx.logic_and(0));
        for (int i = 1; i < ctx.logic_and().size(); i++) {
            result = result | visitLogicAnd(ctx.logic_and(i));
        }
        return result;
    }

    @Override
    public Integer visitLogicAnd(SPLParser.LogicAndContext ctx) {
        int result = visitEquality(ctx.equality(0));
        for (int i = 1; i < ctx.equality().size(); i++) {
            result = result & visitEquality(ctx.equality(i));
        }
        return result;
    }

    @Override
    public Integer visitEquality(SPLParser.EqualityContext ctx) {
        int result = visitComparison(ctx.comparison(0));
        for (int i = 1; i < ctx.comparison().size(); i++) {
            if (ctx.getChild(i).getText().equals("==")) {
                result = result == visitComparison(ctx.comparison(i)) ? 1 : 0;
            } else {
                result = result != visitComparison(ctx.comparison(i)) ? 1 : 0;
            }
        }
        return result;
    }

    @Override
    public Integer visitComparison(SPLParser.ComparisonContext ctx) {
        int result = visitTerm(ctx.term(0));
        for (int i = 1; i < ctx.term().size(); i++) {
            if (ctx.getChild(i).getText().equals(">")) {
                result = result > visitTerm(ctx.term(i)) ? 1 : 0;
            } else if (ctx.getChild(i).getText().equals(">=")) {
                result = result >= visitTerm(ctx.term(i)) ? 1 : 0;
            } else if (ctx.getChild(i).getText().equals("<")) {
                result = result < visitTerm(ctx.term(i)) ? 1 : 0;
            } else {
                result = result <= visitTerm(ctx.term(i)) ? 1 : 0;
            }
        }
        return result;
    }

    @Override
    public Integer visitTerm(SPLParser.TermContext ctx) {
        int result = visitFactor(ctx.factor(0));
        for (int i = 1; i < ctx.factor().size(); i++) {
            if (ctx.getChild(i).getText().equals("+")) {
                result += visitFactor(ctx.factor(i));
            } else {
                result -= visitFactor(ctx.factor(i));
            }
        }
        return result;
    }

    @Override
    public Integer visitFactor(SPLParser.FactorContext ctx) {
        int result = visitUnary(ctx.unary(0));
        for (int i = 1; i < ctx.unary().size(); i++) {
            if (ctx.getChild(i).getText().equals("*")) {
                result *= visitUnary(ctx.unary(i));
            } else {
                result /= visitUnary(ctx.unary(i));
            }
        }
        return result;
    }

    @Override
    public Integer visitUnary(SPLParser.UnaryContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitPrimary(ctx.primary());
        } else {
            if (ctx.getChild(0).getText().equals("-")) {
                return -visitUnary(ctx.unary(0));
            } else {
                return visitUnary(ctx.unary(0)) == 0 ? 1 : 0;
            }
        }
    }

    @Override
    public Integer visitPrimary(SPLParser.PrimaryContext ctx) {
        if (ctx.TRUE() != null) {
            return 1;
        } else if (ctx.FALSE() != null) {
            return 0;
        } else if (ctx.NUMBER() != null) {
            return Integer.parseInt(ctx.NUMBER().getText());
        } else if (ctx.IDENTIFIER() != null) {
            String variableName = ctx.IDENTIFIER().getText();
            if (!variables.containsKey(variableName)) {
                throw new RuntimeException("Variable " + variableName + " not declared.");
            }
            Integer value = variables.get(variableName);
            if (value == null) {
                throw new RuntimeException("Variable " + variableName + " not initialized.");
            }
            return value;
        } else {
            return visitExpression(ctx.expression());
        }
    }
}
