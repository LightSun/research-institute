//
// Created by Administrator on 2019/8/16.
//
#include <stdio.h>
#include <setjmp.h>
#include "../log/logger.h"

static jmp_buf buf;

static void __second(void) {
    LOGD("second");         // 打印
    longjmp(buf, 1);             // 跳回setjmp的调用处 - 使得setjmp返回值为1
}

static void __first(void) {
    __second();             // __second 直接跳转了。
    LOGD("first");          // 不可能执行到此行
}

int main_setjmp() {
    //默认setjmp = 0.
    if (!setjmp(buf)) {
        __first();                // 进入此行前，setjmp返回0
    } else {                    // 当longjmp跳转回，setjmp返回1，因此进入此行
        LOGD("main");       // 打印
    }
    return 0;
}

