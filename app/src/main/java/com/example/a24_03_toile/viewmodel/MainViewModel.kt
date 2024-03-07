package com.example.a24_03_toile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24_03_toile.model.PictureBean
import com.example.a24_03_toile.model.WeatherAPI
import com.example.a24_03_toile.model.pictureList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class MainViewModel : ViewModel() {

    var searchText by mutableStateOf("")
    val myList = mutableStateListOf<PictureBean>()
    var runInProgress by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var lastAction  = {  }

    private var job = Job()


    fun filterList() = myList.filter { it.title.contains(searchText) }


    fun uploadSearchText(newText: String) {
        searchText = newText
        if (searchText.length > 2) {
            runInProgress = true
            job.cancel()
            job = Job()
            viewModelScope.launch(Dispatchers.Default + job) {
                    delay(2000)
                    loadData(true)
            }
        }
    }

    open fun loadData(force: Boolean = false) {//Simulation de chargement de donnée
        if (force || myList.isEmpty()) {
            lastAction = { loadData(force)}
            myList.clear()
            errorMessage = ""
            runInProgress = true

            viewModelScope.launch(Dispatchers.Default) {

                try {

                    WeatherAPI.loadWeatherAround(searchText).map {
                        PictureBean(
                            it.id,
                            it.weather.getOrNull(0)?.icon ?: "",
                            it.name,
                            "Il fait ${it.main.temp}° à ${it.name} avec un vent de ${it.wind.speed}m/s"
                        )
                    }.let {
                        if(it.isEmpty()) {
                            throw Exception("Aucun élément")
                        }
                        myList.addAll(it)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage = "Erreur : ${e.message}"
                }

                runInProgress = false
            }
        }
    }
}

class FakeViewModel : MainViewModel() {
    init {
        myList.clear()
        myList.addAll(pictureList.shuffled()) //Charge la liste en mode mélangé
    }
}