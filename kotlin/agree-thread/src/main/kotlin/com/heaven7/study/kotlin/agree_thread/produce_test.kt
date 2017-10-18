package com.heaven7.study.kotlin.agree_thread

import com.sun.management.jmx.Trace.send
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.ProducerJob
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking


/**
 * 生产消费者: 管道
 */
fun main(args: Array<String>) {
    consumeSquares()
}


private fun testPipeline() = runBlocking {
    val numbers = produceNumbers() // produces integers from 1 and on
    /*val squares = square(numbers) // squares integers
    while (true) {
        println(squares.receive())
    }
    println("Done!")
    squares.cancel()
    numbers.cancel()*/
}

private fun produceNumbers() = produce<Long>(CommonPool) {
    var x = 1L
    while (true) send(x++) // infinite stream of integers starting from 1
}

private fun square(numbers: ReceiveChannel<Int>) = produce<Int>(CommonPool) {
    for (x in numbers) send(x * x)
}

//================================================
private fun consumeNumbers(jbos: ProducerJob<Long>) = runBlocking{
    jbos.consumeEach { println(it) }
    println("Done!")
}
private fun consumeSquares() = runBlocking{
    val squares = produceSquares()
    squares.consumeEach { println(it) }
    println("Done!")
}

private fun produceSquares() = produce<Int>(CommonPool) {
    for (x in 1..7) send(x * x)
}
