import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import gen.*;
public class Main {
    public static void main(String[] args) {
        String expression = "3 + 5 * (10 - 2);";
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