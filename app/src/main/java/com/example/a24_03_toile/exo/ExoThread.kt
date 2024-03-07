package com.example.a24_03_toile.exo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

fun main() {
    exoCoroutine()
}

fun exoCoroutine(){
    val ballot = BallotBoxBean()
    val before = System.currentTimeMillis()

    runBlocking {

        traitement(ballot)
        println("fin traitement 1")
        traitement(ballot)
        println("fin traitement 2")
        withContext(Dispatchers.Default) {
            repeat(10_000) {
                launch {
                    ballot.add1VoiceAndWaitWithDelay()
                }
            }
        }

        println("fin  withContext")


    }

    println("number=${ballot.current}")
    val after = System.currentTimeMillis()
    println("Done in ${after - before} ms")
}

suspend fun traitement(ballot : BallotBoxBean){
    withContext(Dispatchers.Default) {
        repeat(10_000) {
            launch {
                ballot.add1VoiceAndWaitWithDelay()
            }
        }
    }
}

fun exoThread(){
    val ballot = BallotBoxBean()
    val before = System.currentTimeMillis()

    val list = ArrayList<Thread>()

    repeat(100_000) {
        list += thread {
            ballot.add1VoiceAndWait()
        }
    }

    list.forEach { it.join() }

    println("number=${ballot.current}")
    val after = System.currentTimeMillis()
    println("Done in ${after - before} ms")
}

//Classe garantissant un compte ThreadSafe
class BallotBoxBean {
    private val number = AtomicInteger(0)

    fun add1VoiceAndWait() {
        Thread.sleep(2000)
        number.incrementAndGet()
    }

    suspend fun add1VoiceAndWaitWithDelay() {

        delay(2000)
        number.incrementAndGet()
    }

    val current
        get() = number.get()
}