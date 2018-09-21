// Generated from E:/study/github/research-institute/java/study-antlr/src/main/java/com/heaven7/java/study/antlr/javaExpre\JavaExpre.g4 by ANTLR 4.7
package com.heaven7.java.study.antlr.javaExpre;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JavaExpreParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JavaExpreVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#surroundSimpleExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSurroundSimpleExpre(JavaExpreParser.SurroundSimpleExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#simpleExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExpre(JavaExpreParser.SimpleExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#surroundExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSurroundExpre(JavaExpreParser.SurroundExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#expre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpre(JavaExpreParser.ExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#simpleExpreList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExpreList(JavaExpreParser.SimpleExpreListContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#arrayInvoke}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayInvoke(JavaExpreParser.ArrayInvokeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#fieldInvoke}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldInvoke(JavaExpreParser.FieldInvokeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#methodInvoke}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodInvoke(JavaExpreParser.MethodInvokeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#arrayExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayExpre(JavaExpreParser.ArrayExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#fieldExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldExpre(JavaExpreParser.FieldExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#methodExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodExpre(JavaExpreParser.MethodExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#ternaryExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernaryExpre(JavaExpreParser.TernaryExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#surroundTernaryExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSurroundTernaryExpre(JavaExpreParser.SurroundTernaryExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#doubleOrExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleOrExpre(JavaExpreParser.DoubleOrExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#surroundDoubleOrExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSurroundDoubleOrExpre(JavaExpreParser.SurroundDoubleOrExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#doubeAndExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubeAndExpre(JavaExpreParser.DoubeAndExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#surroundDoubeAndExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSurroundDoubeAndExpre(JavaExpreParser.SurroundDoubeAndExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#equalExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualExpre(JavaExpreParser.EqualExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#surroundEqualExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSurroundEqualExpre(JavaExpreParser.SurroundEqualExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#notEqualExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqualExpre(JavaExpreParser.NotEqualExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#surroundNotEqualExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSurroundNotEqualExpre(JavaExpreParser.SurroundNotEqualExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#booleanConstantExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanConstantExpre(JavaExpreParser.BooleanConstantExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#classNameExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassNameExpre(JavaExpreParser.ClassNameExpreContext ctx);
	/**
	 * Visit a parse tree produced by {@link JavaExpreParser#varNameExpre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarNameExpre(JavaExpreParser.VarNameExpreContext ctx);
}