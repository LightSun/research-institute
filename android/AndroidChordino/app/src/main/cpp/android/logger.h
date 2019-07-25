//
// Created by Administrator on 2019/7/25.
//

#ifndef ANDROIDCHORDINO_LOGGER_H
#define ANDROIDCHORDINO_LOGGER_H

#include <android/log.h>
#include "../chordinomedia/chordinomedia.h"

#ifndef LOG_TAG
    #define LOG_TAG "ChordinoExtract"
    #define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
    #define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
    #define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#endif

void log_init();

#endif //ANDROIDCHORDINO_LOGGER_H
