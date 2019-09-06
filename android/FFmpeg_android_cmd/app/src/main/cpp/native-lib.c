

#include <jni.h>
#include <string.h>

#include <stdio.h>
#include <time.h>
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libavfilter/avfilter.h"
#include "libavutil/log.h"

#ifdef ANDROID

#define LOG_TAG    "FFmpeg_cmd"
#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, format, ##__VA_ARGS__)
#define LOGI(format, ...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, format, ##__VA_ARGS__)
#else
#define LOGE(format,...)  printf(LOG_TAG format "\n", ##__VA_ARGS__)
#define LOGI(format,...)  printf(LOG_TAG format "\n", ##__VA_ARGS__)
#endif


int ffmpegExec(int argc, char **argv);

void custom_log(void *ptr, int level, const char *fmt, va_list vl) {
    FILE *fp = fopen("/storage/emulated/0/Android/data/com.heaven7.android.ffmpeg.cmd/files/av_log.txt","a+");
    if (fp) {
        vfprintf(fp, fmt, vl);
        fflush(fp);
        fclose(fp);
    }
}


JNIEXPORT jint JNICALL
Java_com_heaven7_android_ffmpeg_cmd_MainActivity_executeCmd(JNIEnv *env, jobject instance, jint len,
                                                            jobjectArray cmds) {

    av_log_set_callback(custom_log);

    int argc = len;
    char **argv = (char **) malloc(sizeof(char *) * argc);

    int i = 0;
    for (i = 0; i < argc; i++) {
        jstring jstr = (jstring) (*env)->GetObjectArrayElement(env, cmds, i);
        const char *tmp = (char*) (*env)->GetStringUTFChars(env, jstr, 0);
        argv[i] = (char *) malloc(sizeof(char) * 1024);
        strcpy(argv[i], tmp);
    }

    ffmpegExec(argc, argv);

    for (i = 0; i < argc; i++) {
        free(argv[i]);
    }
    free(argv);
    return 0;
}