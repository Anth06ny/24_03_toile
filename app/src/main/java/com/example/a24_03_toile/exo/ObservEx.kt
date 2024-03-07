package com.example.a24_03_toile.exo

import com.example.a24_03_toile.model.CarBean


class MutableLiveData(value : CarBean?) {
    var value : CarBean? = value
        set(newValue) {
            field = newValue
                action?.invoke(newValue)
        }

    var action : ((CarBean?)->Unit )? = null
        set(newValue) {
            field = newValue
            action?.invoke(value)
        }

}


fun main() {

    var car = MutableLiveData(null)
    car.value = CarBean("Seat")

    car.action = {
        println(it)
    }


    car.value = CarBean("Opel")
    car.value = car.value?.copy(marque = "Ferrari")


}