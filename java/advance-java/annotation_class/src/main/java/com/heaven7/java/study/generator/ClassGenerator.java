package com.heaven7.java.study.generator;

import com.heaven7.java.study.BaseGenerator;
import com.heaven7.java.study.Getter;
import com.heaven7.java.study.ProcessorContext;
import com.heaven7.java.study.util.ClassFiler;
import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;

public class ClassGenerator extends BaseGenerator {

    public ClassGenerator(ProcessorContext context) {
        super(context);
    }

    public void generate(Element element) {
        VariableElement ve = (VariableElement) element;
        TypeElement te = (TypeElement) ve.getEnclosingElement();
        final String packageName = getElements().getPackageOf(te).getQualifiedName().toString();
        String fieldName = ve.getSimpleName().toString();
        Getter getter = ve.getAnnotation(Getter.class);

        final TypeSpec.Builder builder = TypeSpec.classBuilder(getContext().getTargetClassName(te) + "__gen")
                .addModifiers(Modifier.PUBLIC);

        builder.addField(FieldSpec.builder(TypeName.get(ve.asType()), fieldName, Modifier.PRIVATE)
                .build());

        builder.addMethod(MethodSpec.methodBuilder("get"+ fieldName)
                .addStatement("return %N", fieldName)
                .addModifiers(getModifiers(getter))
                .build());

        try {
            JavaFile.builder(packageName, builder.build())
                    .build()
                    .writeTo(new ClassFiler(getFiler()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Modifier getModifiers(Getter getter) {
        switch (getter.value()){
            case java.lang.reflect.Modifier.PUBLIC:
                return Modifier.PUBLIC;
            case java.lang.reflect.Modifier.PRIVATE:
                return Modifier.PRIVATE;
            case java.lang.reflect.Modifier.PROTECTED:
                return Modifier.PROTECTED;
        }
        return Modifier.DEFAULT;
    }
}
