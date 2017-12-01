package com.heaven7.java.study.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ModifierVisibilityClassFileTransformer implements IdeaPatcherTransformer {
    @Override
    public boolean supported() {
        return true;
    }

    @Override
    public boolean canRetransform() {
        return true;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("transform(): class = " + className);
        if(className.equals("com/intellij/psi/impl/source/PsiModifierListImpl")){
            return doClass(className, classBeingRedefined, classfileBuffer);
        }
        return null;
    }

    private byte[] doClass(String className, Class<?> classBeingRedefined, byte[] classfileBuffer) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try{
            cl = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));
            CtMethod m = cl.getDeclaredMethod("hasModifierProperty");
            m.insertBefore("System.out.println(\"ModifierVisibilityClassFileTransformer transform success.\")");
            return cl.toBytecode();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(cl != null){
                cl.detach();
            }
        }
        return null;
    }
}
