// Generated from /home/tobias/Desktop/6. Semester/Compilerbau/SPL/src/SPL.g4 by ANTLR 4.12.0
package gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SPLParser}.
 */
public interface SPLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SPLParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(SPLParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(SPLParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(SPLParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(SPLParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(SPLParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(SPLParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(SPLParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(SPLParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(SPLParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(SPLParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(SPLParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(SPLParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#printStmt}.
	 * @param ctx the parse tree
	 */
	void enterPrintStmt(SPLParser.PrintStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#printStmt}.
	 * @param ctx the parse tree
	 */
	void exitPrintStmt(SPLParser.PrintStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(SPLParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(SPLParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(SPLParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(SPLParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SPLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SPLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(SPLParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(SPLParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#logic_or}.
	 * @param ctx the parse tree
	 */
	void enterLogic_or(SPLParser.Logic_orContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#logic_or}.
	 * @param ctx the parse tree
	 */
	void exitLogic_or(SPLParser.Logic_orContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#logic_and}.
	 * @param ctx the parse tree
	 */
	void enterLogic_and(SPLParser.Logic_andContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#logic_and}.
	 * @param ctx the parse tree
	 */
	void exitLogic_and(SPLParser.Logic_andContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#equality}.
	 * @param ctx the parse tree
	 */
	void enterEquality(SPLParser.EqualityContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#equality}.
	 * @param ctx the parse tree
	 */
	void exitEquality(SPLParser.EqualityContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(SPLParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(SPLParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(SPLParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(SPLParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(SPLParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(SPLParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#unary}.
	 * @param ctx the parse tree
	 */
	void enterUnary(SPLParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#unary}.
	 * @param ctx the parse tree
	 */
	void exitUnary(SPLParser.UnaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(SPLParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(SPLParser.PrimaryContext ctx);
}