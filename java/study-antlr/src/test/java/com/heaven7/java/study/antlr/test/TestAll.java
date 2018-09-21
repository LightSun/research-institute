package com.heaven7.java.study.antlr.test;

import com.heaven7.java.study.antlr.java.JavaBaseListener;
import com.heaven7.java.study.antlr.java.JavaLexer;
import com.heaven7.java.study.antlr.java.JavaParser;
import com.heaven7.java.study.antlr.java.SimpleJavaVisitor;
import com.heaven7.java.study.antlr.javaExpre.JavaExpreBaseListener;
import com.heaven7.java.study.antlr.javaExpre.JavaExpreLexer;
import com.heaven7.java.study.antlr.javaExpre.JavaExpreParser;
import com.heaven7.java.study.antlr.javaExpre.SimpleJavaExpreVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

/**
 * @author heaven7
 */
public class TestAll {

    @Test
    public void testJava() {
        //
       // String expre = "String.valueOf(1)";
        String expre = "String. valueOf(a[1]) .test(true, Integer.valueOf(), Integer.valueOf(\"3\"))";

        CodePointCharStream stream = CharStreams.fromString(expre);
        //用 in 构造词法分析器 lexer，词法分析的作用是产生记号
        JavaLexer lexer = new JavaLexer(stream);
        //用词法分析器 lexer 构造一个记号流 tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        //再使用 tokens 构造语法分析器 parser,至此已经完成词法分析和语法分析的准备工作
        JavaParser parser = new JavaParser(tokens);
        parser.addParseListener(new JavaBaseListener());

        SimpleJavaVisitor<Object> visitor = new SimpleJavaVisitor<>();
        //最终调用语法分析器的规则 prog，完成对表达式的验证
        visitor.visit(parser.expression());
    }

    @Test
    public void test1(){
      //  String expre = "String.valueOf(1).test(1,2, Integer.valueOf(\"3\"))";
       // String expre = "String. valueOf(a.b.c[1], a==b, a ? c :d==f) .test(true, Integer.valueOf(), Integer.valueOf(\"3\"))";
        String expre = " z.intVal() == String.valueOf((a))";

        CodePointCharStream stream = CharStreams.fromString(expre);
        //用 in 构造词法分析器 lexer，词法分析的作用是产生记号
        JavaExpreLexer lexer = new JavaExpreLexer(stream);
        //用词法分析器 lexer 构造一个记号流 tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        //再使用 tokens 构造语法分析器 parser,至此已经完成词法分析和语法分析的准备工作
        JavaExpreParser parser = new JavaExpreParser(tokens);
        parser.addParseListener(new JavaExpreBaseListener());

        SimpleJavaExpreVisitor<Object> visitor = new SimpleJavaExpreVisitor<>();
        //最终调用语法分析器的规则 prog，完成对表达式的验证
        visitor.visit(parser.simpleExpre());
    }
}
