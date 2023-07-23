import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import gen.*;
public class Main {
    public static void main(String[] args) {
        String expression = """
                var a = "true";
                var b;
                while (a < b) { print "Hallo";}
                """;
        SPLLexer lexer = new SPLLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SPLParser parser = new SPLParser(tokens);
        SPLParser.ProgramContext ast = parser.program();

        CustomSPLVisitor visitor = new CustomSPLVisitor();
        visitor.visit(ast);
    }
}