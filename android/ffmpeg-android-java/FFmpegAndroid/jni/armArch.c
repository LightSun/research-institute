#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <cpu-features.h>

jstring
Java_com_github_hiteshsondhi88_libffmpeg_ArmArchHelper_cpuArchFromJNI(JNIEnv* env, jobject obj)
{
    // Maximum we need to store here is ARM v7-neon
    // Which is 11 char long, so initializing a character array of length 11
	char arch_info[11] = "";

    // checking if CPU is of ARM family or not
    switch(android_getCpuFamily()){
       case ANDROID_CPU_FAMILY_ARM:
            strcat(arch_info, "armv7");
            break;
       case ANDROID_CPU_FAMILY_ARM64:
            strcat(arch_info, "arm64");
            break;
    }

    // checking if CPU is ARM v7 or not
    /* uint64_t cpuFeatures = android_getCpuFeatures();
    if ((cpuFeatures & ANDROID_CPU_ARM_FEATURE_ARMv7) != 0) {
        strcat(arch_info, "armv7");

        // checking if CPU is ARM v7 Neon
        if((cpuFeatures & ANDROID_CPU_ARM_FEATURE_NEON) != 0) {
            strcat(arch_info, "-neon");
        }
    } */

	return (*env)->NewStringUTF(env, arch_info);
}
