// Generated from C:/Users/Leo/IdeaProjects/SPL/src\SPL.g4 by ANTLR 4.12.0
package gen;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SPLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SPLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SPLParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(SPLParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(SPLParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(SPLParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SPLParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#exprStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmt(SPLParser.ExprStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(SPLParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#printStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStmt(SPLParser.PrintStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#whileStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(SPLParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(SPLParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SPLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(SPLParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#logic_or}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogic_or(SPLParser.Logic_orContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#logic_and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogic_and(SPLParser.Logic_andContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#equality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality(SPLParser.EqualityContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(SPLParser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(SPLParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(SPLParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#unary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(SPLParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(SPLParser.PrimaryContext ctx);
}