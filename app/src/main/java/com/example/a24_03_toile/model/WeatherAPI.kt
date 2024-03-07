package com.example.a24_03_toile.model

import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.InputStreamReader

var toto: String? = "coucou"


fun main() {

//   runBlocking {
//       WeatherAPI.getWeathers("Nice", "blabla", "Toulouse", "Lyon").filterIsInstance(WeatherBean::class).collect {
//               println("Il fait ${it.main.temp}° à ${it.name} avec un vent de ${it.wind.speed}m/s")
//       }
//   }

    println(WeatherAPI.loadWeatherAround("Nice"))

//    val weather = WeatherAPI.loadWeather("Nice")
//    println("Il fait ${weather.main.temp}° à ${weather.name} avec un vent de ${weather.wind.speed}m/s")
}


object WeatherAPI {

    val gson = Gson()
    val client = OkHttpClient()
    const val URL_SERVER = "https://api.openweathermap.org/data/2.5"
    const val REGLAGES = "&appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr"


    fun loadWeatherAround(cityName: String): List<WeatherBean> {
        //Eventuel contrôle
        //Réaliser la requête.
        val json: String = sendGet("/find?&q=$cityName")
        //Parser le JSON avec le bon bean et GSON
        return gson.fromJson(json, WeatherAroundResult::class.java).list.onEach {
            it.weather.getOrNull(0)?.let { weather->
                weather.icon = "https://openweathermap.org/img/wn/${weather.icon}@4x.png"
            }
        }
    }

    fun loadWeather(cityName: String): WeatherBean {
        //Eventuel contrôle
        //Réaliser la requête.
        val json: String = sendGet("/weather?q=$cityName")

        //Parser le JSON avec le bon bean et GSON
        val data: WeatherBean = gson.fromJson(json, WeatherBean::class.java)

        //Eventuel contrôle ou extraction de données

        //Retourner la donnée
        return data
    }

    fun getWeathers(vararg cities:String) = flow {
        cities.forEach {
            try {
                emit(loadWeather(it))
                delay(100)
            }catch(e:Exception) {
                emit(e)
            }
        }
    }


    fun loadWeatherOpti(cityName: String): WeatherBean {
        sendGetOpti(URL_SERVER.format(cityName)).use {
            InputStreamReader(it.body.byteStream()).use {
                return gson.fromJson(it, WeatherBean::class.java)
            }
        }
    }

    //Méthode qui prend en entrée une url, execute la requête
    //Retourne le code HTML/JSON reçu
    fun sendGet(url: String): String {
        println("url : $url")
        //Création de la requête
        val request = Request.Builder().url(URL_SERVER + url + REGLAGES).build()
        //Execution de la requête
        return client.newCall(request).execute().use { //it:Response
            //use permet de fermer la réponse qu'il y ait ou non une exception
            //Analyse du code retour
            if (!it.isSuccessful) {
                throw Exception("Réponse du serveur incorrect :${it.code}")
            }
            //Résultat de la requête
            it.body.string()
        }
    }

    fun sendGetOpti(url: String): Response {
        println("url : $url")
        //Création de la requête
        val request = Request.Builder().url(url).build()
        //Execution de la requête
        val response = client.newCall(request).execute()
        //Analyse du code retour
        return if (!response.isSuccessful) {
            //On ferme la réponse qui n'est plus fermé par le use
            response.close()
            throw Exception("Réponse du serveur incorrect : ${response.code}")
        } else {
            response
        }
    }
}

data class WeatherAroundResult (val list : List<WeatherBean>)

data class WeatherBean(
    val id:Int,
    val main: Main,
    val name: String,
    val weather: List<Weather>,
    val wind: Wind
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String,
    var icon: String,
    val main: String
)

data class Wind(
    val speed: Double
)


