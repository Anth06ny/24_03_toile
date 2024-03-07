package com.example.a24_03_toile.exo


fun main() {


    var v1 = "coucou"
    println(v1.uppercase())

    var v2: String? = "coucou"
    println(v2?.uppercase())

    var v3: String? = null
    println(v3?.uppercase())

    var v5: Boolean = true

    var v4 = v3 + v3

    if (v3.isNullOrBlank()) {
        println("Coucou")
    }

    println(boulangerie(scanNumber("Croissant : "), scanNumber("Croissant : "), scanNumber("Croissant : ")))
}

fun scanText(question: String): String {
    print(question)
    return readlnOrNull() ?: "-"
}

fun scanNumber(question: String) = scanText(question).toIntOrNull() ?: 0

fun boulangerie(nbCroi: Int = 0, nbBag: Int = 0, nbSand: Int = 0) =
    nbCroi * PRICE_C + nbBag * PRICE_B + nbSand * PRICE_S

fun pair(c: Int) = c % 2 == 0
fun myPrint(text: String) = println("#$text#")

fun min(a: Int, b: Int, c: Int) =
    if (a < b && a < c) a
    else if (b < a && b < c) b
    else c
