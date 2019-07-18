//
// Created by Administrator on 2019/7/1.
//

#include <jni.h>
#include <string>
#include <android/log.h>
#include "java_env.h"

#define TAG "JavaEnv"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)

JavaVM *g_jvm;

Registration createRegistration(char *clazz, JNINativeMethod methods[], int len) {
    Registration res;
    res.clazz = clazz;
    res.methods = methods;
    res.len = len;
    return res;
}

int registerMethods(JNIEnv *env, Registration n) {
    LOGD("Registration: len = %d", n.len);
    jclass clazz = env->FindClass(n.clazz);
    if (clazz == nullptr) {
        LOGW("can't find class %s", n.clazz);
        return JNI_ERR;
    }
    int result;
    if ((env)->RegisterNatives(clazz, n.methods, n.len) < 0) {
        LOGW("register method failed");
        result = JNI_ERR;
    } else {
        result = JNI_OK;
    }
    env->DeleteLocalRef(clazz);
    return result;
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    g_jvm = vm;
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGD("JNI_OnUnload ------");
    g_jvm = NULL;
}

JNIEnv *getJNIEnv() {
    JNIEnv *env = NULL;
    if (g_jvm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        LOGD("getJNIEnv failed ! ------");
        return NULL;
    }
    return env;
}

/** if from sub thread . jni-env should attach first. */
JNIEnv *attachJNIEnv() {
    JNIEnv *env = NULL;
    JavaVMAttachArgs args;
    args.version = JNI_VERSION_1_6; // choose your JNI version
    args.name = "N_attachJNIEnv";   // you might want to give the java thread a name
    args.group = NULL;
    if (g_jvm->AttachCurrentThreadAsDaemon(&env, &args) != JNI_OK) {
        LOGD("attachJNIEnv failed ! ------");
        return NULL;
    }
    return env;
}

void detachJNIEnv() {
    g_jvm->DetachCurrentThread();
}