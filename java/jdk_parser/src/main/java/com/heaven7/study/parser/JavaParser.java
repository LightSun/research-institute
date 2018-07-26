package com.heaven7.study.parser;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class JavaParser {

    private JavacFileManager fileManager;
    private JavacTool javacTool;

    public JavaParser() {
        Context context = new Context();
        fileManager = new JavacFileManager(context, true, Charset.defaultCharset());
        javacTool = JavacTool.create();
    }

    public void parseJavaFiles() {
        URL resource = JavaParser.class.getClassLoader().getResource("User.java");
        if(resource == null){
            throw new RuntimeException();
        }
        String src = resource.toString();
        if(src.startsWith("file:/")){
            src = src.substring(src.indexOf("/") + 1);
        }

        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(src);
        JavaCompiler.CompilationTask compilationTask =
                javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            SourceVisitor sourceVisitor = new SourceVisitor();
            for (CompilationUnitTree tree : result) {
                tree.accept(sourceVisitor, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SourceVisitor extends TreeScanner<Void, Void> {

        private String currentPackageName = null;

        @Override
        public Void visitCompilationUnit(CompilationUnitTree node, Void aVoid) {
            return super.visitCompilationUnit(node, aVoid);
        }

        @Override
        public Void visitVariable(VariableTree node, Void aVoid) {
            formatPtrln(
                    "variable name: %s, type: %s, kind: %s, package: %s",
                    node.getName(), node.getType(), node.getKind(), currentPackageName);
            return null;
        }
    }

    public static void formatPtrln(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void main(String[] args) {

        new JavaParser().parseJavaFiles();
    }
}
