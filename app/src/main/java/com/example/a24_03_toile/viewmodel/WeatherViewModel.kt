package com.example.a24_03_toile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.a24_03_toile.model.WeatherAPI
import com.example.a24_03_toile.model.WeatherBean

class WeatherViewModel : ViewModel() {

    var weather : WeatherBean? = null
    var errorMessage = ""

    fun loadWeather(cityName  :String) {
        errorMessage = ""
        weather = null

        try {
            weather = WeatherAPI.loadWeather(cityName)
        }
        catch(e:Exception) {
            e.printStackTrace()
            errorMessage = "Erreur : " + e.message
        }
    }
}