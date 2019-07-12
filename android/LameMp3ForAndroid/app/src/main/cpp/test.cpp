//
// Created by Administrator on 2019/7/12.
//
#include "jni.h"
#include "android/log.h"
#include "chordinomedia.h"
#include "string"

#include "lame_chordino.h"

#define TAG "Lame_Chordino_Test"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)

void *openResult = nullptr;
MediaData data;

std::string &getFormattedStr(std::string &strFormatted, const char *strFormat, va_list arglist)
{
    const int MAX_FORMATTED_STR_LEN = 2048;
    char strResult[MAX_FORMATTED_STR_LEN] = { 0 };
    vsprintf(strResult, strFormat, arglist);
    strFormatted = strResult;
    return strFormatted;
}

void log(const char *tem, ...)
{
    std::string strLog;
    va_list arglist;
    va_start(arglist, tem);
    strLog = getFormattedStr(strLog, tem, arglist);
    va_end(arglist);
    LOGD(strLog.c_str());
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_net_sourceforge_lame_ChordinoMediaPreTest_nInit(JNIEnv *env, jobject instance,
                                                      jstring fileName_) {
    setLog(log);
    const char *fileName = (env)->GetStringUTFChars(fileName_, 0);
    openResult = lame_OpenMedia(fileName, &data);
    (env)->ReleaseStringUTFChars(fileName_, fileName);
    return openResult != nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_net_sourceforge_lame_ChordinoMediaPreTest_nDestroy(JNIEnv *env, jobject instance) {

   lame_ReleaseMedia(openResult);
}

extern "C"
JNIEXPORT jint JNICALL
Java_net_sourceforge_lame_ChordinoMediaPreTest_nReadMediaData(JNIEnv *env, jobject instance,
                                                               jfloatArray pcms_, jint blockSize) {
    jfloat *pcms = (env)->GetFloatArrayElements(pcms_, NULL);
    const int result = lame_ReadMediaData(openResult, pcms, blockSize);
    (env)->ReleaseFloatArrayElements(pcms_, pcms, 0);
    return result;
}