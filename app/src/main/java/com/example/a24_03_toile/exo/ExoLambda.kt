package com.example.a24_03_toile.exo

import java.util.Calendar

fun main() {
    exo3()
}

data class PersonBean(var name:String, var note:Int)

fun exo3(){
    val list = arrayListOf(PersonBean ("toto", 16),
        PersonBean ("Tata", 20),
        PersonBean ("Blabla", 20),
        PersonBean ("Toto", 8),
        PersonBean ("Charles", 14))

    //Pour un affichage de notre choix
    println(list.filter { it.note >=10 }.joinToString("\n") { "-${it.name} : ${it.note}"})

    val isToto : (PersonBean) -> Boolean = { it.name.equals("toto", true)}
    println("\n\nAfficher combien il y a de Toto dans la classe ?")
    println(list.count(isToto))

    println("\n\nAfficher combien de Toto ont plus que la moyenne de la classe")
    val average = list.map { it.note }.average()
    println(list.count{isToto(it) && it.note > average })

    println("\n\nRetirer de la liste ceux ayant la note la plus petite. (Il faut une ArrayList)")
    val min = list.minOf { it.note }
    list.removeIf { it.note == min }

    println("\n\nAfficher par notes croissantes les élèves ayant eu cette note comme sur l'exemple")
    val res = list.groupBy { it.note }
        .entries.sortedBy { it.key }.joinToString("\n") {
        "${it.key} : ${it.value.joinToString(separator = " ") { it.name }}"
    }

    println(res)

}

fun createPairTimeV2()=  Pair(
        Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            set(Calendar.HOUR_OF_DAY, 0)
        }.timeInMillis /1000
        , Calendar.getInstance().apply {
            set(Calendar.MINUTE, 59)
            set(Calendar.HOUR_OF_DAY, 23)
        }.timeInMillis /1000)



data class UserBean(var name:String, var old:Int)

fun exo2(){
    val compareUsersByName = {u1: UserBean, u2: UserBean -> if( u1.name.lowercase() <= u2.name.lowercase()) u1 else u2 }
    val compareUsersByOld = { u1: UserBean, u2: UserBean -> if (u1.old >= u2.old) u1 else u2 }

    val u1 = UserBean ("Bob", 19)
    val u2 = UserBean ("Toto", 45)
    val u3 = UserBean ("Charles", 26)


    println(compareUsers(u1, u2, u3, compareUsersByName)) // UserBean(name=Bob old=19)
    println(compareUsers(u1, u2, u3, compareUsersByOld)) // UserBean(name=Toto old=45)

    val res:UserBean = compareUsers(u1, u2, u3) {a, b ->
       if(Math.abs(30 - a.old) < Math.abs(30 - b.old)) u1 else u2
    }

    println(res)
}

fun compareUsers(u1:UserBean, u2:UserBean, u3:UserBean, comparator : (UserBean, UserBean)->UserBean)
    =comparator(comparator(u1,u2), u3)


fun exo1(){
    var minToMinHour : ((Int?)->Pair<Int, Int>?)? = {
        //if(it != null) Pair(it/60, it%60) else null
        it?.let {Pair(it/60, it%60)}
    }
    minToMinHour = null

    println(minToMinHour?.invoke(124))
}