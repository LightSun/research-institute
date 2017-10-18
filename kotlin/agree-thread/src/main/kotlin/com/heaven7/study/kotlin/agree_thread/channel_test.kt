package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


/**
 * 通道：
 *     通道的概念类似于阻塞队列（blockingQueue）， java中BlockingQueue很好的解决了‘高校安全的数据传输’问题/
 *     take取走， put放入 .
 *     通道和阻塞的最大区别是： 通道有挂起的操作，但不阻塞，同时可以关闭.
 */
fun main(args: Array<String>) {
    //ChannelsDemo().testChannel()
    testClosingAndIterationChannels();
}


/**
 * 测试关闭通道. 直到所有之前的数据被接收才关闭
 */
private fun testClosingAndIterationChannels() = runBlocking {
    val channel = Channel<Int>()
    launch(CommonPool) {
        for (x in 1..5) channel.send(x * x)

        println("before close() => isClosedForReceive = ${channel.isClosedForReceive}")
        channel.close() // 我们结束 sending
        println("after close() => isClosedForReceive = ${channel.isClosedForReceive}")
    }
    // 打印通道中的值，直到通道关闭
    for (x in channel){
        println("${x} => isClosedForReceive = ${channel.isClosedForReceive}")
    }
    println("Done!  => isClosedForReceive = ${channel.isClosedForReceive}")
}

/**
 * Channel<Int>()背后调用的是会合通道RendezvousChannel()，会合通道中没有任何缓冲区。
 * send函数被挂起直到另外一个协程调用receive函数，
 * 然后receive函数挂起直到另外一个协程调用send函数.
 * 它是一个完全无锁的实现。
 */
private class ChannelsDemo {
    fun testChannel() = runBlocking<Unit> {
        val channel = Channel<Int>()
        launch(CommonPool) {
            for (x in 1..10)
                channel.send(x * x)
        }
        println("channel = ${channel}")
        // here we print five received integers:
        repeat(10) {
            println(channel.receive())
        }
        println("Done!")
    }
}