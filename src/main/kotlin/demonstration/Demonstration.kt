package demonstration

import framework.log.WorkshopLogger
import framework.log.WorkshopLogger.logThreadInfo
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author: kaylen.pillay
 **/

/**
 * We can think of thread as a worker. A worker can only perform one action at a time.
 * In this example, the work (i.e Thread), first performs the "Hello" print action and once it has completed that, it
 * then moves on to its next task of the "World" print action.
 *
 * These print actions are known as blocking task. The worker will cannot perform any other task, whilst it is currently
 * executing the blocking. It will either have to complete the task, pause the task or abandon the blocking task.
 *
 * Whilst the println functions are blocking in this example, they execute so fast, that they seem not block the worker
 * (i.e. Thread)
 */

/**
 * Demonstration One
 */
fun main() {
    WorkshopLogger.logThreadInfo("Before we print Hello")
    println("Hello")
    println("World")
    WorkshopLogger.logThreadInfo("After we print World")
}

/**
 * Demonstration Two
 *
 * In this example, the print task take much longer that the native println tasks. The printHello function takes 2
 * seconds to complete and the printWorld takes 1 second to complete. This can be seen in the logs.
 */
//fun main() {
//    WorkshopLogger.logThreadInfo("Before we print Hello")
//    printHello() // This task takes 2 seconds to complete
//    WorkshopLogger.logThreadInfo("After we print Hello")
//    WorkshopLogger.logThreadInfo("Before we print World")
//    printWorld() // This task takes 1 second to complete
//    WorkshopLogger.logThreadInfo("After we print World")
//}
//
//fun printHello() {
//    Thread.sleep(2000) // Simulating a print task that takes 2 seconds to complete
//    println("Hello")
//}
//
//fun printWorld() {
//    Thread.sleep(1000) // Simulating a print task that takes 1 second to complete
//    println("World")
//}

/**
 * Demonstration Three
 *
 * We now would like to perform both print tasks at the same time. If a worker can only perform one action at a time,
 * then in order for us to perform both print tasks at the same time, we would require another worker (i.e. Thread)
 *
 * This example, illustrate two threads executing concurrently. Note, they are created using Java Threads
 */
//fun main() {
//    val helloWorker = Thread {
//        WorkshopLogger.logThreadInfo("Before we print Hello")
//        printHello() // This task takes 2 seconds to complete
//        WorkshopLogger.logThreadInfo("After we print Hello")
//    }
//
//    val worldWorker = Thread {
//        WorkshopLogger.logThreadInfo("Before we print World")
//        printWorld() // This task takes 1 second to complete
//        WorkshopLogger.logThreadInfo("After we print World")
//    }
//
//    helloWorker.start()
//    worldWorker.start()
//}
//
//fun printHello() {
//    Thread.sleep(2000) // Simulating a print task that takes 2 seconds to complete
//    println("Hello")
//}
//
//fun printWorld() {
//    Thread.sleep(1000) // Simulating a print task that takes 1 second to complete
//    println("World")
//}

/**
 * Demonstration Four
 */
//fun main() {
//    var helloWorkerResult = ""
//    var worldWorkerResult = ""
//
//    val helloWorkerResultCallback = TaskCallback { result ->
//        helloWorkerResult = result
//    }
//    val worldWorkerResultCallback = TaskCallback { result ->
//        worldWorkerResult = result
//    }
//
//    val helloWorker = Thread(HelloTaskWithResultCallback(helloWorkerResultCallback))
//    val worldWorker = Thread(WorldTaskWithResultCallback(worldWorkerResultCallback))
//
//    helloWorker.start()
//    worldWorker.start()
//
//    WorkshopLogger.logThreadInfo("Waiting for the helloWorker to complete")
//    helloWorker.join() // Here the main function will WAIT for the helloWorker to FINISH (or DIE)
//
//    WorkshopLogger.logThreadInfo("Waiting for the worldWorker to complete")
//    worldWorker.join() // Here the main function will WAIT for the worldWorker to FINISH (or DIE)
//
//    WorkshopLogger.logThreadInfo("This is the HelloWorker's result: $helloWorkerResult")
//    WorkshopLogger.logThreadInfo("This is the WorldWorker's result: $worldWorkerResult")
//}
//
//fun interface TaskCallback {
//    fun onResult(result: String)
//}
//
//class HelloTaskWithResultCallback(
//    private val callback: TaskCallback
//) : Runnable {
//
//    override fun run() {
//        WorkshopLogger.logThreadInfo("Before we print Hello")
//        val result = getHelloValue()
//        WorkshopLogger.logThreadInfo("After we print Hello")
//        callback.onResult(result) // This task takes 2 seconds to complete
//    }
//
//}
//
//class WorldTaskWithResultCallback(
//    private val callback: TaskCallback
//) : Runnable {
//
//    override fun run() {
//        WorkshopLogger.logThreadInfo("Before we print World")
//        val result = getWorldValue()
//        WorkshopLogger.logThreadInfo("After we print World")
//        callback.onResult(result) // This task takes 1 second to complete
//    }
//
//}
//
//fun getHelloValue(): String {
//    Thread.sleep(2000) // Simulating a task that takes 2 seconds to return
//    return "Hello"
//}
//
//fun getWorldValue(): String {
//    Thread.sleep(1000) // Simulating a task that takes 1 second to return
//    return "World"
//}

/**
 * Demonstration Five
 */
//fun main() {
//    val taskGroupOnIOThreadPool = CoroutineScope(Dispatchers.IO)
//    val taskGroupOnDefaultThreadPool = CoroutineScope(Dispatchers.Default)
//
//    val helloWorker = taskGroupOnIOThreadPool.launch(start = CoroutineStart.LAZY) {
//        WorkshopLogger.logThreadInfo("Before we print Hello")
//        printHello() // This task takes 2 seconds to complete
//        WorkshopLogger.logThreadInfo("After we print Hello")
//    }
//
//    val worldWorker = taskGroupOnDefaultThreadPool.launch(start = CoroutineStart.LAZY) {
//        WorkshopLogger.logThreadInfo("Before we print World")
//        printWorld() // This task takes 1 second to complete
//        WorkshopLogger.logThreadInfo("After we print World")
//    }
//
//    helloWorker.start()
//    worldWorker.start()
//
//    Thread.sleep(5000) // This thread sleeps so that the coroutines have a chance to finish.
//}
//
//fun printHello() {
//    Thread.sleep(2000) // Simulating a print task that takes 2 seconds to complete
//    println("Hello")
//}
//
//fun printWorld() {
//    Thread.sleep(1000) // Simulating a print task that takes 1 second to complete
//    println("World")
//}

/**
 * Demonstration Six
 */
//fun main() {
//    /**
//     * A coroutine is an instance of suspendable computation. It is conceptually similar to a thread, in the sense that
//     * it takes a block of code to run that works concurrently with the rest of the code. However, a coroutine is not
//     * bound to any particular thread. It may suspend its execution in one thread and resume in another one.
//     *
//     */
//
//    demonstrationOne()
//}
//
//fun demonstrationOne() = runBlocking {
//    /**
//     * This example showcases some components of Kotlin Coroutines. In order to create a new coroutine we need to use
//     * the coroutine builder, launch. The launch function creates and immediately schedules the Job to be executed. The
//     * Job object returned by the builder gives you a reference to the created coroutine.
//     */
//
//    launch {
//        delay(1000)
//        println("World!")
//    }
//    println("Hello")
//}

/**
 * Demonstration Seven
 */
//fun main() = runBlocking {
//    val taskList = listOf(
//        async { getHelloValue() },
//        async { getWorldValue() }
//    ).awaitAll()
//
//    WorkshopLogger.logThreadInfo("This is the HelloWorker's result: ${taskList[0]}")
//    WorkshopLogger.logThreadInfo("This is the WorldWorker's result: ${taskList[1]}")
//}
//
//suspend fun getHelloValue(): String = withContext(Dispatchers.IO) {
//    WorkshopLogger.logThreadInfo("Before we print Hello")
//    delay(2000) // Simulating a task that takes 2 seconds to return
//    WorkshopLogger.logThreadInfo("After we print Hello")
//    return@withContext "Hello"
//}
//
//suspend fun getWorldValue(): String = withContext(Dispatchers.Default) {
//    WorkshopLogger.logThreadInfo("Before we print World")
//    delay(1000) // Simulating a task that takes 1 second to return
//    WorkshopLogger.logThreadInfo("After we print World")
//    return@withContext "World"
//}

/**
 * Demonstration Eight
 *
 * Exception handling on coroutine with try catch
 */
//fun main() = runBlocking {
//    launch {
//        try {
//            delay(1000)
//            throw IllegalStateException("Parent Coroutine failed")
//        } catch (e: Exception) {
//            // Do Nothing
//        }
//    }
//
//    return@runBlocking
//}

/**
 * Demonstration Nine
 *
 * Exception handling with children coroutines using try catch
 */
//fun main() = runBlocking {
//    // Parent Coroutine
//    launch {
//        try {
//            // Child 1 Coroutine
//            launch {
//                try {
//                    delay(1000)
//                    throw IllegalStateException("Child 1 failed with exception")
//                } catch (e: Exception) {
//                    // Do Nothing
//                }
//            }
//
//            // Child 2 Coroutine
//            launch {
//                delay(1000)
//                println("Child 2 worked")
//            }
//        } catch (e: Exception) {
//            // Do Nothing
//        }
//    }
//
//    return@runBlocking
//}
