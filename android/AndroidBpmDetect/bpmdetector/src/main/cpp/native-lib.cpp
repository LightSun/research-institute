#include <jni.h>
#include <string>
#include <BPMDetect.h>

using namespace soundtouch;

extern "C"
JNIEXPORT void JNICALL
Java_com_heaven7_android_bpmdetect_BpmDetector__1onNativeRelease(JNIEnv *env, jobject instance,
                                                               jlong ptr) {

    if(ptr != 0){
        BPMDetect* det = reinterpret_cast<BPMDetect *>(ptr);
        delete(det);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_heaven7_android_bpmdetect_BpmDetector__1setSampleData(JNIEnv *env, jobject instance,
                                                             jlong ptr, jshortArray data_,
                                                             jint numSamples) {
    jshort *data = env->GetShortArrayElements(data_, NULL);
    BPMDetect* det = reinterpret_cast<BPMDetect *>(ptr);
    det->inputSamples(data, numSamples);

    env->ReleaseShortArrayElements(data_, data, 0);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_heaven7_android_bpmdetect_BpmDetector__1onNativeInit(JNIEnv *env, jobject instance,
                                                            jint numChannels, jint sampleRate) {
    BPMDetect* detector = new BPMDetect(numChannels, sampleRate);
    return reinterpret_cast<jlong>(detector);
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_heaven7_android_bpmdetect_BpmDetector__1getBmp(JNIEnv *env, jobject instance, jlong ptr) {

    BPMDetect* det = reinterpret_cast<BPMDetect *>(ptr);
    return static_cast<jint>(det->getBpm());
}