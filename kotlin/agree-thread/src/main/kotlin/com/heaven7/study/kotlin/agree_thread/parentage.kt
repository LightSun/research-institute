package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


/**
 * 父子协程
 */
fun main(args: Array<String>) {
    testChildrenCoroutine()
}


fun testChildrenCoroutine()= runBlocking<Unit> {
    val request = launch(CommonPool) {
        println("ContextA1: ${coroutineContext}")

        val job1 = launch(CommonPool) {
            println("job1: 独立的协程上下文!")
            delay(1000)
            println("job1: 不会受到request.cancel()的影响")
        }
        // 继承父上下文：request的context
        val job2 = launch(coroutineContext) {
            println("ContextA2: ${coroutineContext}")
            println("job2: 是request coroutine的子协程")
            delay(1000)
            println("job2: 当request.cancel()，job2也会被取消")
        }
        job1.join()
        job2.join()
    }
    delay(500)
    request.cancel()
    delay(1000)
    println("main: Who has survived request cancellation?")
}