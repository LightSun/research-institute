package com.heaven7.java.study.antlr.javaExpre;

import com.heaven7.java.base.util.Predicates;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * @author heaven7
 */
public class SimpleJavaExpreVisitor<T> extends AbstractParseTreeVisitor<T> implements JavaExpreVisitor<T> {

    //--------------------------------------------------------------------------------------

    @Override
    public T visitSimpleExpre(JavaExpreParser.SimpleExpreContext ctx) {
        log(ctx, "visitSimpleExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitExpre(JavaExpreParser.ExpreContext ctx) {
        log(ctx, "visitExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitSimpleExpreList(JavaExpreParser.SimpleExpreListContext ctx) {
        log(ctx, "visitSimpleExpreList");
        return visitChildren(ctx);
    }

    @Override
    public T visitArrayInvoke(JavaExpreParser.ArrayInvokeContext ctx) {
        log(ctx, "visitArrayInvoke");
        return visitChildren(ctx);
    }

    @Override
    public T visitFieldInvoke(JavaExpreParser.FieldInvokeContext ctx) {
        log(ctx, "visitFieldInvoke");
        return visitChildren(ctx);
    }

    @Override
    public T visitMethodInvoke(JavaExpreParser.MethodInvokeContext ctx) {
        List<TerminalNode> list = ctx.DOT();
        if(!Predicates.isEmpty(list)){
            Token symbol = list.get(0).getSymbol();
            int index = symbol.getStartIndex();
            System.out.println("dot start index = " + index + " ,end index = " + symbol.getStopIndex());
        }
        log(ctx, "visitMethodInvoke, dot.size = " + list.size());
        return visitChildren(ctx);
    }

    @Override
    public T visitArrayExpre(JavaExpreParser.ArrayExpreContext ctx) {
        log(ctx, "visitArrayExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitFieldExpre(JavaExpreParser.FieldExpreContext ctx) {
        log(ctx, "visitFieldExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitMethodExpre(JavaExpreParser.MethodExpreContext ctx) {
        log(ctx, "visitMethodExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitTernaryExpre(JavaExpreParser.TernaryExpreContext ctx) {
        log(ctx, "visitTernaryExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitDoubleOrExpre(JavaExpreParser.DoubleOrExpreContext ctx) {
        log(ctx, "visitDoubleOrExpre");
        return visitChildren(ctx);
    }
    @Override
    public T visitDoubeAndExpre(JavaExpreParser.DoubeAndExpreContext ctx) {
        log(ctx, "visitDoubeAndExpre");
        return visitChildren(ctx);
    }
    @Override
    public T visitEqualExpre(JavaExpreParser.EqualExpreContext ctx) {
        log(ctx, "visitEqualExpre");
        return visitChildren(ctx);
    }
    @Override
    public T visitNotEqualExpre(JavaExpreParser.NotEqualExpreContext ctx) {
        log(ctx, "visitNotEqualExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitBooleanConstantExpre(JavaExpreParser.BooleanConstantExpreContext ctx) {
        log(ctx, "visitBooleanConstantExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitClassNameExpre(JavaExpreParser.ClassNameExpreContext ctx) {
        log(ctx, "visitClassNameExpre");
        return visitChildren(ctx);
    }

    @Override
    public T visitVarNameExpre(JavaExpreParser.VarNameExpreContext ctx) {
        log(ctx, "visitVarNameExpre");
        return visitChildren(ctx);
    }

    //-------------------------------------------------------------------------------
    @Override
    public T visitSurroundSimpleExpre(JavaExpreParser.SurroundSimpleExpreContext ctx) {
        return visitChildren(ctx);
    }
    @Override
    public T visitSurroundExpre(JavaExpreParser.SurroundExpreContext ctx) {
        return visitChildren(ctx);
    }
    @Override
    public T visitSurroundTernaryExpre(JavaExpreParser.SurroundTernaryExpreContext ctx) {
        return visitChildren(ctx);
    }
    @Override
    public T visitSurroundNotEqualExpre(JavaExpreParser.SurroundNotEqualExpreContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitSurroundEqualExpre(JavaExpreParser.SurroundEqualExpreContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitSurroundDoubeAndExpre(JavaExpreParser.SurroundDoubeAndExpreContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitSurroundDoubleOrExpre(JavaExpreParser.SurroundDoubleOrExpreContext ctx) {
        return visitChildren(ctx);
    }




    private void log(RuleContext context, String method){
        // getAltNumber 一直=0.
        //  context.getSourceInterval().toString() 这个是按照所有g4文件定义的小模块 索引来的
        System.out.println(method + ": " + context.getText() + ",rule index = " + context.getRuleIndex());
    }
}
