package com.example.a24_03_toile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.a24_03_toile.databinding.ActivityMainBinding
import com.example.a24_03_toile.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val model by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btLoad.setOnClickListener {

            val cityName = binding.etCityName.text.toString()
            binding.progressBar.isVisible = true
            binding.tvError.isVisible = false

            lifecycleScope.launch(Dispatchers.Default) {

                model.loadWeather(cityName)

                runOnUiThread {
                    refreshScreen()
                    binding.progressBar.isVisible = false
                }
            }
        }

        refreshScreen()
    }

    fun refreshScreen() {
        binding.tvData.text = "Il fait ${model.weather?.main?.temp ?: "-"}° à ${model.weather?.name ?: "-"} avec un vent de ${
            model.weather?.wind
                ?.speed ?: "-"
        } m/s"

        model.weather?.weather?.getOrNull(0)?.icon?.let {
            Picasso.get().load("https://openweathermap.org/img/wn/$it@4x.png")
                .into(binding.imageView)
        } ?: run {
            binding.imageView.setImageDrawable(null)
        }

        binding.tvError.text = model.errorMessage
        binding.tvError.isVisible = model.errorMessage.isNotBlank()
    }
}