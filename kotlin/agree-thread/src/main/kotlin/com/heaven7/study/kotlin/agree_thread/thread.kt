package com.heaven7.study.kotlin.agree_thread

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger


fun main(args: Array<String>) {
    val threadId = AtomicInteger()
    Thread(Runnable {
        println("test thread....")
    }, "CommonPool-worker-${threadId.incrementAndGet()}").apply { isDaemon = true }
}

private fun createPlainPool(): ExecutorService {
    val threadId = AtomicInteger()
    return Executors.newFixedThreadPool(2) {
        //相当于通过下面这个创建 一个匿名ThreadFactory， 并且通过给定的runnable(it所代表)和线程名称。
        //并且apply 一个代码块。设置为守护线程.
        Thread(it, "CommonPool-worker-${threadId.incrementAndGet()}").apply { isDaemon = true }
    }
}