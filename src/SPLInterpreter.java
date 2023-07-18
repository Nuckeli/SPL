import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import gen.*;


public class SPLInterpreter extends SPLBaseVisitor<Void> {
    private final Environment environment;

    public SPLInterpreter() {
        environment = new Environment();
    }

    public void interpret(ParseTree tree) {
        visit(tree);
    }

    @Override
    public Void visitVarDecl(SPLParser.VarDeclContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        if (environment.isVariableDeclared(varName)) {
            throw new RuntimeException("Variable " + varName + " already declared.");
        }
        Object varValue = visit(ctx.expression());
        environment.declareVariable(varName, varValue);
        return null;
    }

    @Override
    public Void visitAssignment(SPLParser.AssignmentContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        if (!environment.isVariableDeclared(varName)) {
            throw new RuntimeException("Variable " + varName + " not declared.");
        }
        Object varValue = visit(ctx.assignment());
        environment.assignVariable(varName, varValue);
        return null;
    }

}
