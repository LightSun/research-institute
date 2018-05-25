#include <jni.h>
#include <string>
#include <android/log.h>
#include <android/bitmap.h>
#include <android/native_window_jni.h>
#include <android/native_window.h>
#include <string.h>

#define  LOG_TAG    "NativeSurfaceTest"
#define  LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,FORMAT,##__VA_ARGS__);
#define  LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,FORMAT,##__VA_ARGS__);
#define  LOGD(FORMAT,...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,FORMAT, ##__VA_ARGS__)

ANativeWindow* nativeWindow = 0;
AndroidBitmapInfo info;

extern "C"
JNIEXPORT void JNICALL
Java_com_heaven7_vida_research_sample_NativeSurfaceTest_updateFrame(JNIEnv *env, jobject instance,
                                                                    jint width, jint height) {
    int result = ANativeWindow_setBuffersGeometry(nativeWindow, width, height, WINDOW_FORMAT_RGBA_8888);
    if(result == 0){
        //success
    }else{
        //failed
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_heaven7_vida_research_sample_NativeSurfaceTest_setSurface(JNIEnv *env, jobject instance,
                                                                   jobject surface, jint width,
                                                                   jint height) {
    nativeWindow = ANativeWindow_fromSurface(env, surface);
    //设置缓冲区的属性（宽、高、像素格式）
    ANativeWindow_setBuffersGeometry(nativeWindow, width, height, WINDOW_FORMAT_RGBA_8888);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_heaven7_vida_research_sample_NativeSurfaceTest_draw(JNIEnv *env, jobject instance,
                                                             jbyteArray imageBits_) {
    jboolean copy = JNI_TRUE;
    jbyte *imageBits = env->GetByteArrayElements(imageBits_, &copy);
    //-----------------
    ANativeWindow_Buffer windowBuffer;
    ANativeWindow_lock(nativeWindow, &windowBuffer, NULL);
    __uint16_t * line = (__uint16_t *) windowBuffer.bits;
    // modify pixels with image processing algorithm
    for (int y = 0; y < windowBuffer.height; y++) {
        for (int x = 0; x < windowBuffer.width; x++) {
            //RGB565的灰色十进制为1280
            line[x] = 57083;
        }
        line = line + windowBuffer.stride;
    }
    ANativeWindow_unlockAndPost(nativeWindow);
    //-----------------
    env->ReleaseByteArrayElements(imageBits_, imageBits, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_heaven7_vida_research_sample_NativeSurfaceTest_nDestroy(JNIEnv *env, jobject instance) {

    if(nativeWindow != 0){
        ANativeWindow_release(nativeWindow);
        nativeWindow = 0;
    }
}

//https://stackoverflow.com/questions/24263799/correct-save-window-buffer-to-android-bitmap
void drawBitmap(JNIEnv *env, ANativeWindow_Buffer *buffer, jobject bitmap) {
    void *pixels;

    LOGI("saving buffer to bitmap");

    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return;
    }
    LOGI("w = %d, h = %d, format = %d", info.width, info.height, info.format);

    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE( "Failed to lock pixles for bitmap");
        return;
    }
    unsigned char *toBuffer = (unsigned char *) buffer->bits;
    unsigned char *fromBuffer = (unsigned char*)pixels;

    for (int i = 0; i< info.height; i++) {
        memcpy(toBuffer, fromBuffer, info.width * 4);
        fromBuffer += info.width * 4;
        toBuffer += buffer->stride * 4;
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_heaven7_vida_research_sample_NativeSurfaceTest_drawBitmap(JNIEnv *env, jobject instance,
                                                                   jobject bitmap) {
    ANativeWindow_Buffer windowBuffer;
    ANativeWindow_lock(nativeWindow, &windowBuffer, NULL);
    drawBitmap(env, &windowBuffer, bitmap);
    ANativeWindow_unlockAndPost(nativeWindow);
}