package com.heaven7.java.study.antlr.java;


import org.antlr.v4.runtime.RuleContext;

/**
 * @author heaven7
 */
public class SimpleJavaVisitor<T> extends JavaBaseVisitor<T> {

    @Override
    public T visitExpression(JavaParser.ExpressionContext ctx) {
        log(ctx, "visitExpression");
        return super.visitExpression(ctx);
    }

    @Override
    public T visitType(JavaParser.TypeContext ctx) {
        log(ctx, "visitType");
        return super.visitType(ctx);
    }

    @Override
    public T visitAndExpression(JavaParser.AndExpressionContext ctx) {
        log(ctx, "visitAndExpression");
        return super.visitAndExpression(ctx);
    }

    @Override
    public T visitExpressionList(JavaParser.ExpressionListContext ctx) {
        log(ctx, "visitExpressionList");
        return super.visitExpressionList(ctx);
    }

    @Override
    public T visitExclusiveOrExpression(JavaParser.ExclusiveOrExpressionContext ctx) {
        log(ctx, "visitExclusiveOrExpression");
        return super.visitExclusiveOrExpression(ctx);
    }

    @Override
    public T visitAdditiveExpression(JavaParser.AdditiveExpressionContext ctx) {
        log(ctx, "visitAdditiveExpression");
        return super.visitAdditiveExpression(ctx);
    }

    @Override
    public T visitCastExpression(JavaParser.CastExpressionContext ctx) {
        log(ctx, "visitCastExpression");
        return super.visitCastExpression(ctx);
    }

    @Override
    public T visitConditionalExpression(JavaParser.ConditionalExpressionContext ctx) {
        log(ctx, "visitConditionalExpression");
        return super.visitConditionalExpression(ctx);
    }

    @Override
    public T visitConstantExpression(JavaParser.ConstantExpressionContext ctx) {
        log(ctx, "visitConstantExpression");
        return super.visitConstantExpression(ctx);
    }

    @Override
    public T visitEqualityExpression(JavaParser.EqualityExpressionContext ctx) {
        log(ctx, "visitEqualityExpression");
        return super.visitEqualityExpression(ctx);
    }

    @Override
    public T visitMultiplicativeExpression(JavaParser.MultiplicativeExpressionContext ctx) {
        log(ctx, "visitMultiplicativeExpression");
        return super.visitMultiplicativeExpression(ctx);
    }

    @Override
    public T visitParExpression(JavaParser.ParExpressionContext ctx) {
        log(ctx, "visitParExpression");
        return super.visitParExpression(ctx);
    }

    @Override
    public T visitRelationalExpression(JavaParser.RelationalExpressionContext ctx) {
        log(ctx, "visitRelationalExpression");
        return super.visitRelationalExpression(ctx);
    }

    @Override
    public T visitShiftExpression(JavaParser.ShiftExpressionContext ctx) {
        log(ctx, "visitShiftExpression");
        return super.visitShiftExpression(ctx);
    }

    @Override
    public T visitStatementExpression(JavaParser.StatementExpressionContext ctx) {
        log(ctx, "visitStatementExpression");
        return super.visitStatementExpression(ctx);
    }

    @Override
    public T visitUnaryExpression(JavaParser.UnaryExpressionContext ctx) {
        log(ctx, "visitUnaryExpression");
        return super.visitUnaryExpression(ctx);
    }

    @Override
    public T visitUnaryExpressionNotPlusMinus(JavaParser.UnaryExpressionNotPlusMinusContext ctx) {
        log(ctx, "visitUnaryExpressionNotPlusMinus");
        return super.visitUnaryExpressionNotPlusMinus(ctx);
    }

    @Override
    public T visitConditionalAndExpression(JavaParser.ConditionalAndExpressionContext ctx) {
        log(ctx, "visitConditionalAndExpression");
        return super.visitConditionalAndExpression(ctx);
    }

    @Override
    public T visitConditionalOrExpression(JavaParser.ConditionalOrExpressionContext ctx) {
        log(ctx, "visitConditionalOrExpression");
        return super.visitConditionalOrExpression(ctx);
    }

    @Override
    public T visitInclusiveOrExpression(JavaParser.InclusiveOrExpressionContext ctx) {
        log(ctx, "visitInclusiveOrExpression");
        return super.visitInclusiveOrExpression(ctx);
    }

    @Override
    public T visitInstanceOfExpression(JavaParser.InstanceOfExpressionContext ctx) {
        log(ctx, "visitInstanceOfExpression");
        return super.visitInstanceOfExpression(ctx);
    }

    private void log(RuleContext context, String method){
        System.out.println(method + ": " + context.getText());
    }
}
