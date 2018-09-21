package com.heaven7.java.study.antlr;

import com.heaven7.java.study.antlr.javaExpre.JavaExpreBaseVisitor;
import com.heaven7.java.study.antlr.javaExpre.JavaExpreParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * @author heaven7
 */
public class EvaluateJavaExpreVisitor<T> extends JavaExpreBaseVisitor<T> {

    private final MultiEvaluateContext mContexts;

    public EvaluateJavaExpreVisitor(MultiEvaluateContext context) {
        this.mContexts = context;
    }

    @Override
    public T visitMethodInvoke(JavaExpreParser.MethodInvokeContext ctx) {
        List<TerminalNode> list = ctx.DOT();
        return null;
    }
}
