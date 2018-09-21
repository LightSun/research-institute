package com.heaven7.java.study.antlr;

import com.heaven7.java.study.antlr.javaExpre.JavaExpreLexer;
import com.heaven7.java.study.antlr.javaExpre.JavaExpreParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * @author heaven7
 */
public class Evaluator {

    private final MultiEvaluateContext mContexts = new MultiEvaluateContext();

    public Evaluator withEvaluateContext(IEvaluateContext context) {
        mContexts.addEvaluateContext(context);
        return this;
    }

    public <R> R evaluate(String expre) {
        CodePointCharStream stream = CharStreams.fromString(expre);
        CommonTokenStream tokens = new CommonTokenStream(new JavaExpreLexer(stream));

        JavaExpreParser parser = new JavaExpreParser(tokens);
       // parser.addParseListener(new JavaExpreBaseListener());

        EvaluateJavaExpreVisitor<R> visitor = new EvaluateJavaExpreVisitor<>(mContexts);
        return visitor.visit(parser.simpleExpre());
    }
}
