package com.heaven7.java.ast;

import com.heaven7.java.base.util.ResourceLoader;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Map;

public class JdtTest {

    public static void main(String[] args) {
        ASTParser parser = ASTParser.newParser(AST.JLS8); //设置Java语言规范版本
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setCompilerOptions(null);
        parser.setResolveBindings(true);

        Map<String, String> compilerOptions = JavaCore.getOptions();
        compilerOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8); //设置Java语言版本
        compilerOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        compilerOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        parser.setCompilerOptions(compilerOptions); //设置编译选项

        String src = ResourceLoader.getDefault().loadFileAsString(null, "./Person.java");
        parser.setSource(src.toCharArray());
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);  //下个断点可以看看cu的types成员就是整个语法树
        System.out.println(cu);

        //        List commentList = result.getCommentList();
//        PackageDeclaration package1 = result.getPackage();
//        List importList = result.imports();
//        TypeDeclaration type = (TypeDeclaration) result.types().get(0);
//        FieldDeclaration[] fieldList = type.getFields();
//        MethodDeclaration[] methodList = type.getMethods();
//        Block method_block=methodList[1].getBody();
//        TryStatement try_stmt=(TryStatement)method_block.statements().get(0);
//        ForStatement for_stmt=(ForStatement)try_stmt.getBody().statements().get(0);
//        ExpressionStatement express_stmt=(ExpressionStatement) ((Block)for_stmt.getBody()).statements().get(0);

    }
}