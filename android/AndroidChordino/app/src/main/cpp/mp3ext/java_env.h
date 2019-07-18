//
// Created by Administrator on 2019/7/1.
//

#ifndef COMMONLUAAPP_JAVA_ENV_H
#define COMMONLUAAPP_JAVA_ENV_H

#include <jni.h>
#define SIG_JSTRING "Ljava/lang/String;"

typedef struct Registration{
    char* clazz;
    JNINativeMethod* methods;
    int len;
}Registration;

Registration createRegistration(char* clazz, JNINativeMethod methods[], int len);

int registerMethods(JNIEnv* env,Registration n);

JNIEnv *getJNIEnv();

JNIEnv *attachJNIEnv();

void detachJNIEnv();


#endif //COMMONLUAAPP_JAVA_ENV_H
