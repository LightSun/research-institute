package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis


//执行2个协程代码
fun main(args: Array<String>) {
    testSequential()
}

/**
 * 异步执行,返回Deferred
     public interface Deferred<out T> : Job {

      //当协程在计算过程中有异常failed 或被取消，返回true。 这也意味着isActive等于 false ，同时 isCompleted等于 true
        val isCompletedExceptionally: Boolean
        val isCancelled: Boolean

          //等待此延迟任务完成，而不阻塞线程；如果延迟任务完成, 则返回结果值或引发相应的异常。
        public suspend fun await(): T

        public fun <R> registerSelectAwait(select: SelectInstance<R>, block: suspend (T) -> R)
        public fun getCompleted(): T
        @Deprecated(message = "Use `isActive`", replaceWith = ReplaceWith("isActive"))
        public val isComputing: Boolean get() = isActive
    }
 */
fun testAsync() = runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = async(CommonPool) { doJob1() }
        val two = async(CommonPool) { doJob2() }
        println("最终结果： ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}
//顺序执行
fun testSequential() = runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = doJob1()
        val two = doJob2()
        println("[testSequential] 最终结果： ${one + two}")
    }
    println("[testSequential] Completed in $time ms")
}


suspend fun doJob1(): Int {
    println("Doing Job1 ...")
    delay(1000L) // 此处模拟我们的工作代码
    println("Job1 Done")
    return 10
}

suspend fun doJob2(): Int {
    println("Doing Job2 ...")
    delay(1000L) // 此处模拟我们的工作代码
    println("Job2 Done")
    return 20
}