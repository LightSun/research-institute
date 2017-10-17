package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit

/**
        协程（轻量级线程）: https://studygolang.com/articles/11134
 */
fun main(args: Array<String>) {
    firstCoroutineDemo0();
}

/**
 * 等同于
 *  launch(CommonPool, CoroutineStart.DEFAULT, {
        delay(3000L, TimeUnit.MILLISECONDS)
        println("Hello, ")
    })
 */
fun firstCoroutineDemo0() {
    var job = launch(CommonPool) {
        //delay 类似java Thread.sleep. 但是不会阻塞线程
        /**
         * 这个函数在非协程中，不能用. 而且不能在Thread 对象中调用这个delay函数
         */
        delay(3000L, TimeUnit.MILLISECONDS)
        println("Hello,")
    }
    println("World!")
    Thread.sleep(5000L)
}
