package com.heaven7.test;

import javassist.bytecode.*;
import org.junit.Test;

/**
 * @author heaven7
 */
public class JavaAssistTest2 {

    @Test
    public void testClassFileWriter(){
        /**
         package sample;
         public class Test {
         public int value;
         public long value2;
         public Test() { super(); }
         public int one() { return 1; }
         }
         */
        ClassFileWriter cfw = new ClassFileWriter(ClassFile.JAVA_4, 0);
        ClassFileWriter.ConstPoolWriter cpw = cfw.getConstPool();

        ClassFileWriter.FieldWriter fw = cfw.getFieldWriter();
        fw.add(AccessFlag.PUBLIC, "value", "I", null);
        fw.add(AccessFlag.PUBLIC, "value2", "J", null);

        int thisClass = cpw.addClassInfo("sample/Test");
        int superClass = cpw.addClassInfo("java/lang/Object");
        //int superInterface = cpw.addClassInfo("com/heaven7/test/ITest");
        int sysClass = cpw.addClassInfo("java/lang/System");
        int printClass = cpw.addClassInfo("java/io/PrintStream");
        int out = cpw.addStringInfo("out");

        ClassFileWriter.MethodWriter mw = cfw.getMethodWriter();

        mw.begin(AccessFlag.PUBLIC, MethodInfo.nameInit, "()V", null, null);
        mw.add(Opcode.ALOAD_0);
        mw.add(Opcode.INVOKESPECIAL);
        int signature = cpw.addNameAndTypeInfo(MethodInfo.nameInit, "()V");
        mw.add16(cpw.addMethodrefInfo(superClass, signature));
        mw.add(Opcode.RETURN);
        mw.codeEnd(1, 1);
        mw.end(null, null);

        mw.begin(AccessFlag.PUBLIC, "one", "()I", null, null);
        mw.add(Opcode.ICONST_1);
        mw.add(Opcode.IRETURN);
        mw.codeEnd(1, 1);
        mw.end(null, null);

        mw.begin(AccessFlag.PUBLIC, "print", "(Ljava/lang/String)V", null, null);
        int pid = cpw.addNameAndTypeInfo(out, printClass);
        int field = cpw.addFieldrefInfo(sysClass, pid);
        mw.add(Opcode.GETSTATIC);
        mw.add16(field);
        mw.add(Opcode.ALOAD_1);
        mw.add(Opcode.INVOKEDYNAMIC);
        int sig = cpw.addNameAndTypeInfo("println", "(Ljava/lang/String)V");
        mw.add(cpw.addMethodrefInfo(printClass, sig));
        mw.add(Opcode.RETURN);
        mw.codeEnd(2, 2);
        mw.end(null, null);

        byte[] classfile = cfw.end(AccessFlag.PUBLIC, thisClass, superClass,
                null, null);

        Class<?> clazz = new TestClassLoader().defineClass("sample.Test", classfile);
        try {
            Object o = clazz.newInstance();
            Object result = clazz.getMethod("one").invoke(o);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
