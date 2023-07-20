import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import gen.*;
public class Main {
    public static void main(String[] args) {
        String expression = """
                //Euclidean algorithm: Calculate the greatest common divisor of two numbers
                                
                // Input
                var b = 24.0;
                b = 3;
                var x = 1337;
                var s = "Super krasser String der hoffentlich erkannt wird;"
                                
                // Here we go
                while (a != b) {
                	if (a > b) {
                		a = a - b;
                	} else {
                		b = b - a;
                	}
                }
                var c = 0;
                // Result
                print a;
                """;
        SPLLexer lexer = new SPLLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SPLParser parser = new SPLParser(tokens);
        SPLParser.ProgramContext ast = parser.program();

        CustomSPLVisitor visitor = new CustomSPLVisitor();
        visitor.visit(ast);
    }
}