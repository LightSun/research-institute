// Generated from E:/study/github/research-institute/java/study-antlr/src/main/java/com/heaven7/java/study/antlr/javaExpre\JavaExpre.g4 by ANTLR 4.7
package com.heaven7.java.study.antlr.javaExpre;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JavaExpreParser}.
 */
public interface JavaExpreListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#surroundSimpleExpre}.
	 * @param ctx the parse tree
	 */
	void enterSurroundSimpleExpre(JavaExpreParser.SurroundSimpleExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#surroundSimpleExpre}.
	 * @param ctx the parse tree
	 */
	void exitSurroundSimpleExpre(JavaExpreParser.SurroundSimpleExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#simpleExpre}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExpre(JavaExpreParser.SimpleExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#simpleExpre}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExpre(JavaExpreParser.SimpleExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#surroundExpre}.
	 * @param ctx the parse tree
	 */
	void enterSurroundExpre(JavaExpreParser.SurroundExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#surroundExpre}.
	 * @param ctx the parse tree
	 */
	void exitSurroundExpre(JavaExpreParser.SurroundExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#expre}.
	 * @param ctx the parse tree
	 */
	void enterExpre(JavaExpreParser.ExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#expre}.
	 * @param ctx the parse tree
	 */
	void exitExpre(JavaExpreParser.ExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#simpleExpreList}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExpreList(JavaExpreParser.SimpleExpreListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#simpleExpreList}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExpreList(JavaExpreParser.SimpleExpreListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#arrayInvoke}.
	 * @param ctx the parse tree
	 */
	void enterArrayInvoke(JavaExpreParser.ArrayInvokeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#arrayInvoke}.
	 * @param ctx the parse tree
	 */
	void exitArrayInvoke(JavaExpreParser.ArrayInvokeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#fieldInvoke}.
	 * @param ctx the parse tree
	 */
	void enterFieldInvoke(JavaExpreParser.FieldInvokeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#fieldInvoke}.
	 * @param ctx the parse tree
	 */
	void exitFieldInvoke(JavaExpreParser.FieldInvokeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#methodInvoke}.
	 * @param ctx the parse tree
	 */
	void enterMethodInvoke(JavaExpreParser.MethodInvokeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#methodInvoke}.
	 * @param ctx the parse tree
	 */
	void exitMethodInvoke(JavaExpreParser.MethodInvokeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#arrayExpre}.
	 * @param ctx the parse tree
	 */
	void enterArrayExpre(JavaExpreParser.ArrayExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#arrayExpre}.
	 * @param ctx the parse tree
	 */
	void exitArrayExpre(JavaExpreParser.ArrayExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#fieldExpre}.
	 * @param ctx the parse tree
	 */
	void enterFieldExpre(JavaExpreParser.FieldExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#fieldExpre}.
	 * @param ctx the parse tree
	 */
	void exitFieldExpre(JavaExpreParser.FieldExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#methodExpre}.
	 * @param ctx the parse tree
	 */
	void enterMethodExpre(JavaExpreParser.MethodExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#methodExpre}.
	 * @param ctx the parse tree
	 */
	void exitMethodExpre(JavaExpreParser.MethodExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#ternaryExpre}.
	 * @param ctx the parse tree
	 */
	void enterTernaryExpre(JavaExpreParser.TernaryExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#ternaryExpre}.
	 * @param ctx the parse tree
	 */
	void exitTernaryExpre(JavaExpreParser.TernaryExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#surroundTernaryExpre}.
	 * @param ctx the parse tree
	 */
	void enterSurroundTernaryExpre(JavaExpreParser.SurroundTernaryExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#surroundTernaryExpre}.
	 * @param ctx the parse tree
	 */
	void exitSurroundTernaryExpre(JavaExpreParser.SurroundTernaryExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#doubleOrExpre}.
	 * @param ctx the parse tree
	 */
	void enterDoubleOrExpre(JavaExpreParser.DoubleOrExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#doubleOrExpre}.
	 * @param ctx the parse tree
	 */
	void exitDoubleOrExpre(JavaExpreParser.DoubleOrExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#surroundDoubleOrExpre}.
	 * @param ctx the parse tree
	 */
	void enterSurroundDoubleOrExpre(JavaExpreParser.SurroundDoubleOrExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#surroundDoubleOrExpre}.
	 * @param ctx the parse tree
	 */
	void exitSurroundDoubleOrExpre(JavaExpreParser.SurroundDoubleOrExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#doubeAndExpre}.
	 * @param ctx the parse tree
	 */
	void enterDoubeAndExpre(JavaExpreParser.DoubeAndExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#doubeAndExpre}.
	 * @param ctx the parse tree
	 */
	void exitDoubeAndExpre(JavaExpreParser.DoubeAndExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#surroundDoubeAndExpre}.
	 * @param ctx the parse tree
	 */
	void enterSurroundDoubeAndExpre(JavaExpreParser.SurroundDoubeAndExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#surroundDoubeAndExpre}.
	 * @param ctx the parse tree
	 */
	void exitSurroundDoubeAndExpre(JavaExpreParser.SurroundDoubeAndExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#equalExpre}.
	 * @param ctx the parse tree
	 */
	void enterEqualExpre(JavaExpreParser.EqualExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#equalExpre}.
	 * @param ctx the parse tree
	 */
	void exitEqualExpre(JavaExpreParser.EqualExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#surroundEqualExpre}.
	 * @param ctx the parse tree
	 */
	void enterSurroundEqualExpre(JavaExpreParser.SurroundEqualExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#surroundEqualExpre}.
	 * @param ctx the parse tree
	 */
	void exitSurroundEqualExpre(JavaExpreParser.SurroundEqualExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#notEqualExpre}.
	 * @param ctx the parse tree
	 */
	void enterNotEqualExpre(JavaExpreParser.NotEqualExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#notEqualExpre}.
	 * @param ctx the parse tree
	 */
	void exitNotEqualExpre(JavaExpreParser.NotEqualExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#surroundNotEqualExpre}.
	 * @param ctx the parse tree
	 */
	void enterSurroundNotEqualExpre(JavaExpreParser.SurroundNotEqualExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#surroundNotEqualExpre}.
	 * @param ctx the parse tree
	 */
	void exitSurroundNotEqualExpre(JavaExpreParser.SurroundNotEqualExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#booleanConstantExpre}.
	 * @param ctx the parse tree
	 */
	void enterBooleanConstantExpre(JavaExpreParser.BooleanConstantExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#booleanConstantExpre}.
	 * @param ctx the parse tree
	 */
	void exitBooleanConstantExpre(JavaExpreParser.BooleanConstantExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#classNameExpre}.
	 * @param ctx the parse tree
	 */
	void enterClassNameExpre(JavaExpreParser.ClassNameExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#classNameExpre}.
	 * @param ctx the parse tree
	 */
	void exitClassNameExpre(JavaExpreParser.ClassNameExpreContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaExpreParser#varNameExpre}.
	 * @param ctx the parse tree
	 */
	void enterVarNameExpre(JavaExpreParser.VarNameExpreContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaExpreParser#varNameExpre}.
	 * @param ctx the parse tree
	 */
	void exitVarNameExpre(JavaExpreParser.VarNameExpreContext ctx);
}