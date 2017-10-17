package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.text.SimpleDateFormat
import java.util.*

/**
 * 作用： 直到所有处于runBlockingd的代码执行完毕
 *
 * 该runBlocking函数不是用来当作普通协程函数使用的，
 * 它的设计主要是用来桥接普通阻塞代码和挂起风格的（suspending style）的非阻塞代码的,
 * 例如用在 main 函数中，或者用于测试用例代码中。see test of [RunBlockingTest]
 */
fun main(args: Array<String>) = runBlocking<Unit> {
    // 主协程
    println("${format(Date())}: T0")

    // 启动协程.异步的
    launch(CommonPool) {
        //在common thread pool中创建协程
        println("${format(Date())}: T1")
        delay(3000L)
        println("${format(Date())}: T2 Hello,")
    }
    println("${format(Date())}: T3 World!") //  当子协程被delay，主协程仍然继续运行

    delay(5000L)

    println("${format(Date())}: T4")
}

private fun format(date: Date): String {
    return SDF.format(date) //kotlin 中没有new
}

val SDF = SimpleDateFormat()