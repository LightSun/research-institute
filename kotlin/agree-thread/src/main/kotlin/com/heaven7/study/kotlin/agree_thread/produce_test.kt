package com.heaven7.study.kotlin.agree_thread

import com.sun.management.jmx.Trace.send
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.buildSequence
import kotlin.text.Typography.prime


/**
 * 生产消费者: 管道
 */
fun main(args: Array<String>) {
    //consumeSquares()
    //testPipeline()

    //producePrimesSequences()
    //testBuffer()
    println(fibonacci.take(16).toList())
}
//======================= 构建无穷惰性序列 ====================
private val fibonacci = buildSequence { //buildSequence 创建协程
    yield(1L)
    var current = 1L
    var next = 1L
    while (true) {
        /**
         * yield能做到按需调用(惰性)
         */
        yield(next)
        val tmp = current + next
        current = next
        next = tmp
    }
}

//============================= 通道缓冲区 ====================
private fun testBuffer() = runBlocking<Unit> {
    val channel = Channel<Int>(4) // 创建一个缓冲区容量为4的通道
    launch(context) {
        repeat(10) {
            println("Sending $it")
            channel.send(it) // 当缓冲区已满的时候， send将会挂起(相当于阻塞)
        }
    }
    delay(1000)
}


//===================================================
/**
 * 管道与无穷质数序列
 */
private fun producePrimesSequences() = runBlocking {
    var producerJob = numbersProducer(context, 2)

    while (true) {
        val prime = producerJob.receive()
        print("${prime} \t")
        producerJob = filterPrimes(context, producerJob, prime)
    }
}

private fun numbersProducer(context: CoroutineContext, start: Int) = produce<Int>(context) {
    var n = start
    while (true) send(n++) // infinite stream of integers from start
}

private fun filterPrimes(context: CoroutineContext, numbers: ReceiveChannel<Int>, prime: Int) = produce<Int>(context) {
    for (x in numbers) if (x % prime != 0) send(x)
}

//=========================================================
//通道 :生产无限序列
private fun testPipeline() = runBlocking {
    val numbers = produceNumbers() // produces integers from 1 and on
    val squares = square(numbers) // squares integers
    while (true) {
        try {
            println("receive " + squares.receive())
        }catch (e: ClosedReceiveChannelException){
            e.printStackTrace()
        }
    }
    println("Done!")
    numbers.cancel()
    squares.cancel()
    println("active = ${squares.isActive}, isClosedForReceive = ${squares.isClosedForReceive}," +
            " isCompleted = ${squares.isCompleted}, isEmpty = ${squares.isEmpty}")
}

private fun produceNumbers() = produce<Long>(CommonPool) {
    var x = 1L
    while (true) send(x++) // infinite stream of integers starting from 1
}

private fun <T : ReceiveChannel<Long>> square(numbers:  T) = produce<Long>(CommonPool) {
    for (x in numbers) {
        println("$x")
        send(x * x)
    }
}

//================================================
/**
 * 生产者-消费者模式
 */
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
