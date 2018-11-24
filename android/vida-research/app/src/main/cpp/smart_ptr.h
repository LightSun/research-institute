//
// Created by Administrator on 2018/11/22 0022.
//

#ifndef VIDA_RESEARCH_SMART_PTR_H
#define VIDA_RESEARCH_SMART_PTR_H

#include "stdio.h"
#include "jni.h"
#include <stdio.h>
#include <sstream>
#include <iostream>
#include <vector>

//see android frameworks/base/include/utils/RefBase.h
using namespace std;

template <class T>
class LightRefBase
{
public:
    inline LightRefBase() : mCount(0) { }
    inline void incStrong(const void* id) const {
       // android_atomic_inc(&mCount);
    }
    inline void decStrong(const void* id) const {
       /* if (android_atomic_dec(&mCount) == 1) {
            delete static_cast<const T*>(this);
        }*/
    }
    //! DEBUGGING ONLY: Get current strong ref count.
    inline int32_t getStrongCount() const {
        return mCount;
    }

protected:
    inline ~LightRefBase() { }

private:
    mutable volatile int32_t mCount;
};


template <typename T>
class SmartPtr2{

public:

    SmartPtr2(T *p = 0):ptr(p){
        count = new int(1) ;
    }// 第一次创建的时候，引数肯定是1

    SmartPtr2(const SmartPtr2 & rhs):ptr(rhs.ptr),count(rhs.count){
        ++*rhs.count ;
    }

    T &operator*(){return *ptr;}

    T* operator->(){return ptr;}

    SmartPtr2 &operator=(const SmartPtr2 & rhs){

        if(ptr == rhs.ptr)
            return *this ;

        if(--*count == 0){
            delete ptr ;
            delete count ;
        }
        ++*rhs.count ;
        count = rhs.count ;
        ptr = rhs.ptr ;
        return *this;
    }

    ~SmartPtr2(){
        if(--*count==0) {
            delete ptr;
            delete count;
        }
    }

private:

    T *ptr ;
    int *count ;

};

#endif //VIDA_RESEARCH_SMART_PTR_H
