import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import gen.*;
public class Main {
    public static void main(String[] args) {
        String expression = """
                // Print the first n Fibonacci numbers

                // Input
                var n = 10;

                // Here we go
                var a = 0;\s
                var b = 1;\s

                var i = 1;
                var next;
                while (i <= n) {
                	print a;
                	next = a + b;
                	a = b;
                	b = next;
                	i = i + 1;
                }\s
                """;
        SPLLexer lexer = new SPLLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SPLParser parser = new SPLParser(tokens);
        SPLParser.ProgramContext ast = parser.program();

        CustomSPLVisitor visitor = new CustomSPLVisitor();
        visitor.visit(ast);
    }
}