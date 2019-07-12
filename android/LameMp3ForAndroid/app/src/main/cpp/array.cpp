//
// Created by Administrator on 2019/7/11.
//

#include "array.h"

/**
 * copy float array from src to dest
 * @param s1 the src array
 * @param s1_start the start of src
 * @param dst the dest array
 * @param dst_start the start of dest.
 * @param copySize the copied size
 */
void copyFloatArray(float* s1, int s1_start, float* dst, int dst_start, int copySize){
    for (int i = 0; i < copySize; ++i) {
        dst[dst_start + i] = s1[s1_start + i];
    }
}



