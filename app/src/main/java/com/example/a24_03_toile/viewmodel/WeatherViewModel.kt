package com.example.a24_03_toile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24_03_toile.model.WeatherAPI
import com.example.a24_03_toile.model.WeatherBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    var weather  = MutableLiveData<WeatherBean?>(null)
    var errorMessage = MutableLiveData("")
    var runInProgress = MutableLiveData(false)
    var searchText = MutableLiveData("")

    fun loadWeather(){
        loadWeather(searchText.value ?: "")
    }

    fun loadWeather(cityName  :String) {
        errorMessage.postValue("")
        weather.postValue(null)
        runInProgress.postValue(true)

        viewModelScope.launch(Dispatchers.Default) {
            try {
                weather.postValue(WeatherAPI.loadWeather(cityName))
            }
            catch(e:Exception) {
                e.printStackTrace()
                errorMessage.postValue("Erreur : " + e.message)
            }
            runInProgress.postValue(false)
        }

    }
}