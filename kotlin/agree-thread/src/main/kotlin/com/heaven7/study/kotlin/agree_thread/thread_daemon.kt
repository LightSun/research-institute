package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

/**
 * 测试: 协程和守护线程. 俩者很像
 */
fun main(args: Array<String>) {
   // testDaemon2()
    testDaemon1()
}


private fun testDaemon2() {
    val t = Thread({
        repeat(100) { i ->
            println("I'm sleeping $i ...")
            Thread.sleep(500L)
        }
    })
    t.isDaemon = true // 必须在启动线程前调用,否则会报错：Exception in thread "main" java.lang.IllegalThreadStateException
    t.start()
    Thread.sleep(2000L) // just quit after delay
}

/**
 * 协程
 */
private fun testDaemon1() = runBlocking {
    launch(CommonPool) {
        repeat(100) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(2000L) // just quit after delay
}