import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import gen.*;
public class Main {
    public static void main(String[] args) {
        String expression = """
                //Euclidean algorithm: Calculate the greatest common divisor of two numbers
                                
                // Input
                var x;
                var x;
                var b = 24.0;
                                
                // Here we go
                while (a != b) {
                	if (a > b) {
                		a = a - b;
                	} else {
                		b = b - a;
                	}
                }
                                
                // Result
                print a;
                """;
        SPLLexer lexer = new SPLLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SPLParser parser = new SPLParser(tokens);
        ParseTree tree = parser.program();
        SPLInterpreter interpreter = new SPLInterpreter();
        interpreter.interpret(tree);
        // System.out.println("Parse tree: " + tree.toStringTree(parser));

        //SemanticAnalyzer analyzer = new SemanticAnalyzer();
        //analyzer.visit(tree);
    }
}