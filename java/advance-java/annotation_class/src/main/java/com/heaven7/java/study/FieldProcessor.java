package com.heaven7.java.study;

import com.heaven7.java.study.generator.ClassGenerator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//@AutoService(Processor.class)
public class FieldProcessor extends AbstractProcessor {

    private ProcessorContext mContext;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Getter.class.getName());
        return Collections.unmodifiableSet(set);
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        mContext = new ProcessorContext(env.getFiler(), env.getElementUtils(),
                env.getTypeUtils(), new ProcessorPrinter(env.getMessager()));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(annotations.isEmpty()){
            return false;
        }
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Getter.class);
        ClassGenerator cg = new ClassGenerator(mContext);
        //just test one
        for(Element element : set){
            cg.generate(element);
        }
        return false;
    }

}
