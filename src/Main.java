import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import gen.*;
public class Main {
    public static void main(String[] args) {
        String expression = """
                var global = 100;
                var i = 0;
                i = i + 1;
                if (i == 1){
                	print "Basic assignment: ok";
                }\s

                i = 0;
                while (i < 10){
                	i = i + 1;
                }
                if (i == 10){
                	print "Basic looping: ok";
                }\s


                {
                	var i = 0;
                	if (i == 0){
                		print "Shadowing: ok";
                	}
                	if (global == 100){
                		print "Outer scope: ok";
                	}
                }
                if (i == 10){
                	print "Nested scope: ok";
                }


                var log1 = false and false;
                var log2 = false and true;
                var log3 = true and false;
                var log4 = true and true;

                if (!log1 and !log2 and !log3 and log4){
                 	print "Logic and: ok";
                }

                log1 = false or false;
                log2 = false or true;
                log3 = true or false;
                log4 = true or true;

                if (!log1 and log2 and log3 and log4){
                 	print "Logic or: ok";
                }
                """;
        SPLLexer lexer = new SPLLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SPLParser parser = new SPLParser(tokens);
        SPLParser.ProgramContext ast = parser.program();

        CustomSPLVisitor visitor = new CustomSPLVisitor();
        visitor.visit(ast);
    }
}