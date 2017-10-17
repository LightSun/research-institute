package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.*

/**
 * 调度器. 可以调度到一个线程池.
 *         限制在特定的线程，
 */
fun main(args: Array<String>) {
    testDispatchersAndThreads()
}

fun testDispatchersAndThreads() = runBlocking {
    val jobs = arrayListOf<Job>()
    jobs += launch(Unconfined) {
        // 未作限制 -- 将会在 main thread 中执行
        println("Unconfined: I'm working in thread ${Thread.currentThread()}")
    }
    jobs += launch(coroutineContext) {
        // 父协程的上下文 ： runBlocking coroutine
        println("context: I'm working in thread ${Thread.currentThread()}")
    }
    jobs += launch(CommonPool) {
        // 调度指派给 ForkJoinPool.commonPool
        println("CommonPool: I'm working in thread ${Thread.currentThread()}")
    }
    jobs += launch(newSingleThreadContext("MyOwnThread")) {
        // 将会在这个协程自己的新线程中执行
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread()}")
    }
    jobs.forEach { it.join() }
}
/**
 * 从上面的结果，我们可以看出：
使用无限制的Unconfined上下文的协程运行在主线程中；
继承了 runBlocking {...} 的context的协程继续在主线程中执行；
而CommonPool在ForkJoinPool.commonPool中；
我们使用newSingleThreadContext函数新建的协程上下文，该协程运行在自己的新线程Thread[MyOwnThread,5,main]中。
        */