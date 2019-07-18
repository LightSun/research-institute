//
// Created by Administrator on 2019/7/18.
//

#include "mp3_upperlayyer.h"
#include "java_env.h"

#define CLASS_NAME "com/heaven7/android/chordino/MediaParser"
jmethodID mid_readData = nullptr;
jclass clazz;
jobject parser;

JNIEnv *getRealEnv() {
    JNIEnv *pEnv = getJNIEnv();
    if (pEnv == nullptr) {
        pEnv = attachJNIEnv();
    }
    return pEnv;
}

void *up_openMedia(const char *file, MediaData *out) {
    JNIEnv *const env = getRealEnv();
    clazz = env->FindClass(CLASS_NAME);
    jmethodID mid_cons = env->GetMethodID(clazz, "<init>", "()V");
    parser = env->NewObject(clazz, mid_cons);

    jmethodID mid_open = env->GetMethodID(clazz, "openMedia", "(Ljava/lang/String;)V");
    mid_readData = env->GetMethodID(clazz, "readMedia", "(I[I)[F");

    jstring const fileStr = env->NewStringUTF(file);
    env->CallVoidMethod(parser, mid_open, fileStr);
    env->DeleteLocalRef(fileStr);

    out->channelCount = 2;
    out->sampleRate = 48000;
    return parser;
}

void up_logError(const char *file, void *openResult) {

}

/**
 * read the media data for target count. every is a float
 * @param openResult
 * @param filebuf
 * @param count
 * @return the count of read sample count
 */
int up_readMediaData(void *openResult, float *filebuf, int count) {
    JNIEnv *const pEnv = getRealEnv();
    jintArray jia = pEnv->NewIntArray(1);
    jfloatArray jfa = (jfloatArray)pEnv->CallObjectMethod(parser, mid_readData, count, jia);
    jint ji[1];
    pEnv->GetIntArrayRegion(jia, 0, 1, ji);
    pEnv->DeleteLocalRef(jia);

    int size = pEnv->GetArrayLength(jfa);
    jfloat buf[1];
    for(int i = 0 ; i < size ; i ++){
        pEnv->GetFloatArrayRegion(jfa, i, 1, buf);
        filebuf[i] = buf[0];
    }
    pEnv->DeleteLocalRef(jfa);
    return ji[0];
}

void up_releaseMedia(void *openResult) {
    JNIEnv *const pEnv = getRealEnv();
    pEnv->DeleteLocalRef(parser);
    pEnv->DeleteLocalRef(clazz);
   // detachJNIEnv(); //when running can't detach
}

