package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout

/**
 * 设置协程超时时间
 */
fun main(args: Array<String>) {
    try {
        testTimeouts()
    }catch (e: CancellationException ){
        //
    }
}

private fun testTimeouts() = runBlocking {
    withTimeout(3000L) {
        repeat(100) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}