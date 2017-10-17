package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.*
import java.util.concurrent.TimeUnit


fun main(args: Array<String>) {
    //firstCoroutineDemo()
    //testJoinCoroutine()
    testJoinCoroutine2()
}

private fun firstCoroutineDemo() {
    launch(CommonPool) {
        delay(3000L, TimeUnit.MILLISECONDS)
        println("[firstCoroutineDemo] Hello, 1")
    }

    launch(CommonPool, CoroutineStart.DEFAULT, {
        delay(3000L, TimeUnit.MILLISECONDS)
        println("[firstCoroutineDemo] Hello, 2")
    })
    println("[firstCoroutineDemo] World!") //上面的协程没执行完毕就结束了
}

/**
 * 直到所有代码执行完毕.
 */
private fun testJoinCoroutine() = runBlocking<Unit> {
    // Start a coroutine
    val c1 = launch(CommonPool) {
        fc1()
    }

    val c2 = launch(CommonPool) {
        fc2()
    }
   //c1, c2, main 的代码会有竞争
    println("Main Thread: ${Thread::currentThread}")
    println("Hello,")
    println("Hi,")
    println("c1 is active: ${c1.isActive}  ${c1.isCompleted}")
    println("c2 is active: ${c2.isActive}  ${c2.isCompleted}")
}

//=====================================
//让所有协程 参数main的时间顺序来. 用join方法
private fun testJoinCoroutine2() = runBlocking<Unit> {
    // Start a coroutine
    val c1 = launch(CommonPool) {
        fc1()
    }

    val c2 = launch(CommonPool) {
        fc2()
    }

    waitFc1(c1, c2)

    c1.join() // the main thread will wait until child coroutine completes
    afterFc1Done(c1, c2)
    c2.join() // the main thread will wait until child coroutine completes
    afterFc2Done(c1, c2)
}

private fun afterFc1Done(c1: Job, c2: Job) {
    println("Hi,")
    afterFc2Done(c1, c2)
}

private fun waitFc1(c1: Job, c2: Job) {
    println("Main Thread: ${Thread.currentThread()}")
    println("Hello,")

    afterFc2Done(c1, c2)
}

private fun afterFc2Done(c1: Job, c2: Job) {
    println("c1 is active: ${c1.isActive}  isCompleted: ${c1.isCompleted}")
    println("c2 is active: ${c2.isActive}  isCompleted: ${c2.isCompleted}")
}

private suspend fun fc2() {
    println("C2 Thread: ${Thread.currentThread()}")
    println("C2 Start")
    delay(5000L)
    println("C2 World! 2")
}

private suspend fun fc1() {
    println("C1 Thread: ${Thread.currentThread()}")
    println("C1 Start")
    delay(3000L)
    println("C1 World! 1")
}