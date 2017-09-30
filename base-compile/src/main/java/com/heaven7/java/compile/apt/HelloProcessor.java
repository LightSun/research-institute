package com.heaven7.java.compile.apt;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.heaven7.java.compile.apt.Hello")
public class HelloProcessor extends AbstractProcessor {

    // 计数器, 用于计算 process() 方法运行了几次
    private int count = 1;

    // 用于写文件
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    // 处理编译时注解的方法
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("start process, count = " + count++);
        // 获得所有类
        Set<? extends Element> rootElements = roundEnv.getRootElements();
        System.out.println("all class:");

        for (Element rootElement : rootElements) {
            System.out.println("  " + rootElement.getSimpleName());
        }

        // 获得有注解的元素, 这里 Hello 只能修饰类, 所以只有类
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Hello.class);
        System.out.println("annotated class:");
        for (Element element : elementsAnnotatedWith) {
            String className = element.getSimpleName().toString();
            System.out.println("  " + className);

            String output = element.getAnnotation(Hello.class).name();
            // 产生的动态类的名字
            String newClassName = className + "_New";
            // 写 java 文件
            createFile(newClassName, output);
        }
        return true;
    }

    private void createFile(String className, String output) {
        StringBuilder cls = new StringBuilder();
        cls.append("package apt;\n\npublic class ")
                .append(className)
                .append(" {\n  public static void main(String[] args) {\n")
                .append("    System.out.println(\"")
                .append(output)
                .append("\");\n  }\n}");
        try {
            JavaFileObject sourceFile = filer.createSourceFile("apt." + className);
            Writer writer = sourceFile.openWriter();
            writer.write(cls.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}