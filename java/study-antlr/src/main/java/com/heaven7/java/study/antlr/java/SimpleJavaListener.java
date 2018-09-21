package com.heaven7.java.study.antlr.java;

/**
 * @author heaven7
 */
public class SimpleJavaListener extends JavaBaseListener {

    @Override
    public void enterExpression(JavaParser.ExpressionContext ctx) {
        System.out.println(ctx.getText());
        super.enterExpression(ctx);
    }
    @Override
    public void exitExpression(JavaParser.ExpressionContext ctx) {
        super.exitExpression(ctx);
    }

}
