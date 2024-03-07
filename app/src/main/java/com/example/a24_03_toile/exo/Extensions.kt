package com.example.a24_03_toile.exo

import com.google.gson.Gson

fun main() {
}

//fun Any?.toJson() = Gson().toJson(this ?: "{}" )
fun Any?.toJson() = this?.let { Gson().toJson(it) } ?: "{}"