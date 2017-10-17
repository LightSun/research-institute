package com.heaven7.study.kotlin.agree_thread

import kotlinx.coroutines.experimental.*


/**
 * 协程的取消.
 */
fun main(args: Array<String>) {
    //testCancellation()
    //testCooperativeCancellation1()
    //testCooperativeCancellation2()
    //testCooperativeCancellation3()
   // finallyCancelDemo()
    testNonCancellable()
}

//协程 执行不可取消的代码块
private fun testNonCancellable() = runBlocking {
    val job = launch(CommonPool) {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            //协程 执行不可取消的代码块
            run(NonCancellable) {
                println("I'm running finally")
                delay(1000L)
                println("And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(2000L)
    println("main: I'm tired of waiting!")
    job.cancel()
    delay(2000L)
    println("main: Now I can quit.")
}

//finally
private fun finallyCancelDemo() = runBlocking {
    val job = launch(CommonPool) {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("I'm running finally")
            delay(1000L)
            println("And I've delayed for 1 sec ?") //挂起后.没执行就退出了
        }
    }
    delay(2000L)
    println("Before cancel, Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")
    job.cancel()
    println("After cancel, Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")
    delay(2000L)
    println("main: Now I can quit.")
}

/***
 * 可以取消的携程
 */
private fun testCooperativeCancellation3() = runBlocking {
    //启动 协程
    var job = launch {
        var nextPrintTime = 0L
        var i = 0
        while (i < 20) { // computation loop

            //这里可以try也可以不try
            try {
                var d = yield()  //被取消时抛出 CancellationException异常。
                when(d){
                    Unit->  println("is unit")
                    else ->{
                        println("not unit: $d")
                    }
                }
                val currentTime = System.currentTimeMillis()
                if (currentTime >= nextPrintTime) {
                    println("I'm sleeping ${i++} ... CurrentThread: ${Thread.currentThread()}")
                    nextPrintTime = currentTime + 500L
                }
            } catch (e: CancellationException) {
             //   println("$i ${e.message}")
            }finally {
                //finally 将会正常执行
            }
        }
    }
    delay(3000L)
    println("CurrentThread: ${Thread.currentThread()}")
    println("Before cancel, Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")
    val b1 = job.cancel() // cancels the job
    println("job cancel1: $b1")
    println("After Cancel, Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")

    delay(3000L)
    val b2 = job.cancel() // cancels the job, job already canceld, return false
    println("job cancel2: $b2")

    println("main: Now I can quit.")
}


/***
 * 可以取消的携程
 */
private fun testCooperativeCancellation2() = runBlocking {
    //启动 协程
    var job = launch {
        var nextPrintTime = 0L
        var i = 0
        while (i < 20) { // computation loop

            //通过运行时判断状态可以取消  协程
            if(!isActive){
                return@launch
            }

            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextPrintTime) {
                println("I'm sleeping ${i++} ... CurrentThread: ${Thread.currentThread()}")
                nextPrintTime = currentTime + 500L
            }
        }
    }
    delay(3000L)
    println("CurrentThread: ${Thread.currentThread()}")
    println("Before cancel, Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")
    val b1 = job.cancel() // cancels the job
    println("job cancel1: $b1")
    println("After Cancel, Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")

    delay(3000L)
    val b2 = job.cancel() // cancels the job, job already canceld, return false
    println("job cancel2: $b2")

    println("main: Now I can quit.")
}

//==============================================================================================

//即使调用了cancel函数，当前的job状态isAlive是false了，但是协程的代码依然一直在运行，并没有停止。
private fun testCooperativeCancellation1() = runBlocking<Unit> {
    val job = launch(CommonPool) {
        var nextPrintTime = 0L
        var i = 0
        while (i < 20) { // computation loop
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextPrintTime) {
                println("I'm sleeping ${i++} ... CurrentThread: ${Thread.currentThread()}")
                nextPrintTime = currentTime + 500L
            }
        }
    }
    delay(3000L)
    println("CurrentThread: ${Thread.currentThread()}")
    println("Before cancel, Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")

    val b1 = job.cancel() // cancels the job
    println("job cancel1: $b1")
    println("After Cancel, Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")

    delay(30000L)

    val b2 = job.cancel() // cancels the job, job already canceld, return false
    println("job cancel2: $b2")

    println("main: Now I can quit.")
}

//可以取消
private fun testCancellation() = runBlocking<Unit> {
    val job = launch(CommonPool) {
        repeat(1000) { i ->
            println("I'm sleeping $i ... CurrentThread: ${Thread.currentThread()}")
            delay(500L)
        }
    }
    delay(1300L)
    println("CurrentThread: ${Thread.currentThread()}")
    println("Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")
    val b1 = job.cancel() // cancels the job
    println("job cancel: $b1")
    delay(1300L)
    println("Job is alive: ${job.isActive}  Job is completed: ${job.isCompleted}")

    //只能cancel一次， 后面再cancel 返回false
    val b2 = job.cancel() // cancels the job, job already canceld, return false
    println("job cancel: $b2")
    if(!b2){
        println("already canceled");
    }

    println("main: Now I can quit.")
}