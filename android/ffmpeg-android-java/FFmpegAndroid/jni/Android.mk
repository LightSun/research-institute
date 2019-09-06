LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ARM_ARCH

LOCAL_SRC_FILES := armArch.c \
                   cpu-features.c

LOCAL_CFLAGS := -DHAVE_NEON=1
#LOCAL_STATIC_LIBRARIES := cpufeatures
LOCAL_LDLIBS := -llog

LOCAL_CFLAGS += -pie -fPIE
LOCAL_LDFLAGS += -pie -fPIE

include $(BUILD_SHARED_LIBRARY)
# $(call import-add-path,$LOCAL_PATH)
# $(call import-module,cpufeatures)
