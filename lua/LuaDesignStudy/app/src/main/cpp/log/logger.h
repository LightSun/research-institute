//
// Created by Administrator on 2019/8/16.
//

#ifndef LUADESIGNSTUDY_LOGGER_H
#define LUADESIGNSTUDY_LOGGER_H

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __ANDROID_NDK__
#include "android/log.h"
#define LOG_TAG "LuaNativeStudy"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#endif

#ifdef __cplusplus
};
#endif


#endif //LUADESIGNSTUDY_LOGGER_H
