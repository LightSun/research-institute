//
// Created by Administrator on 2018/8/14 0014.
//

#include <windows.h>
#include "xxx.h"
int main() {
    //step: 1 gen dll
    HINSTANCE handle = LoadLibrary("E:\\study\\github\\research-institute\\cpp\\simple1\\cmake-build-debug\\libxxx.dll");
    typedef void (*pointer)(void);
    pointer f;
    f = (pointer)GetProcAddress(handle, "hello");
    f();
    FreeLibrary(handle);
    return 0;
}