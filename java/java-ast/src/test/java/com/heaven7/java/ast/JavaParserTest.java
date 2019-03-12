package com.heaven7.java.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.heaven7.java.base.util.ResourceLoader;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.github.javaparser.ast.Modifier.Keyword.PRIVATE;
import static com.github.javaparser.ast.Modifier.Keyword.PUBLIC;
import static com.github.javaparser.ast.Modifier.Keyword.STATIC;

/** @author heaven7 */
public class JavaParserTest {

  @Test
  public void testParse() {
    String src = ResourceLoader.getDefault().loadFileAsString(null, "./Person.java");
    CompilationUnit compilationUnit = StaticJavaParser.parse(src);
    Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName("Person");
    List<MethodDeclaration> methods = classA.get().getMethods();
    System.out.println();
  }

  @Test
  public void testAnalyze() {
    String src = ResourceLoader.getDefault().loadFileAsString(null, "./Person.java");
    CompilationUnit compilationUnit = StaticJavaParser.parse(src);

    compilationUnit
        .findAll(FieldDeclaration.class)
        .stream()
        .filter(f -> !f.isStatic())
        .forEach(
            f ->
                System.out.println(
                    "Check field at line " + f.getRange().map(r -> r.begin.line).orElse(-1)));
  }

  @Test
  public void testTransform() {
    String src = ResourceLoader.getDefault().loadFileAsString(null, "./Person.java");
    CompilationUnit compilationUnit = StaticJavaParser.parse(src);
    compilationUnit
        .findAll(ClassOrInterfaceDeclaration.class)
        .stream()
        .filter(
            c -> !c.isInterface() && c.isAbstract() && !c.getNameAsString().startsWith("Abstract"))
        .forEach(
            c -> {
              String oldName = c.getNameAsString();
              String newName = "Abstract" + oldName;
              System.out.println("Renaming class " + oldName + " into " + newName);
              c.setName(newName);
            });
  }

  @Test
  public void testGenerate() {

    CompilationUnit compilationUnit = new CompilationUnit();
    ClassOrInterfaceDeclaration myClass = compilationUnit.addClass("MyClass").setPublic(true);
    myClass.addField(int.class, "A_CONSTANT = 0", PUBLIC, STATIC);
    myClass.addField(String.class, "name", PRIVATE);
    String code = myClass.toString();
    System.out.println(code);
  }
}
