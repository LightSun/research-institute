#include <jni.h>
#include <string>

#include "chordextract.h"

extern "C" JNIEXPORT jstring JNICALL
 Java_com_heaven7_android_chordino_MainActivity_stringFromJNI(
         JNIEnv *env,
         jobject obj) {
     std::string hello = "Hello from C++";
     return env->NewStringUTF(hello.c_str());
 }

 extern "C" JNIEXPORT void JNICALL
 Java_com_heaven7_android_chordino_MainActivity_extractChords(
         JNIEnv *env,
         jobject obj, jstring simpleName, jstring filename) {

     char* sn = const_cast<char*>(env->GetStringUTFChars(simpleName, 0));
     char* file = const_cast<char*>(env->GetStringUTFChars(filename, 0));
     char* arr[2];
     arr[0] = sn;
     arr[1] = file;
     main(2, arr);
 }
