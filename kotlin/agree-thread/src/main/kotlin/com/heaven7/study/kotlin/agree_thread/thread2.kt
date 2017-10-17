package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


fun main(args: Array<String>) {
    //testThread()
    testLightWeightCoroutine();
}

/**
 * 这里创建了太多的线程。会ome(OutOfMemory)或者执行的时间很长，因为会去创建系统的线程
 *  能创建的线程数的具体计算公式如下：
Number of Threads = (MaxProcessMemory - JVMMemory - ReservedOsMemory) / (ThreadStackSize)

参数	说明
MaxProcessMemory	指的是一个进程的最大内存
JVMMemory	JVM内存
ReservedOsMemory	保留的操作系统内存
ThreadStackSize	线程栈的大小

 *
 */
fun testThread() {
    val jobs = List(100_1000) {
        Thread({
            Thread.sleep(1000L)
            print(".")
        })
    }
    jobs.forEach { it.start() }
    jobs.forEach { it.join() }
}
//使用协程。性能明显优于 testThread().
// 一般1-2s执行完毕
fun testLightWeightCoroutine() = runBlocking {
    val jobs = List(100_000) {
        // create a lot of coroutines and list their jobs
        launch(CommonPool) {
            delay(1000L)
            print(".")
        }
    }
    jobs.forEach { it.join() } // wait for all jobs to complete
}