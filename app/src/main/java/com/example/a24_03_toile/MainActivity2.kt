package com.example.a24_03_toile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.a24_03_toile.databinding.ActivityMainBinding
import com.example.a24_03_toile.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso

class MainActivity2 : AppCompatActivity() {


    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val model by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //observe()

//        binding.btLoad.setOnClickListener {
//            val cityName = binding.etCityName.text.toString()
//            model.loadWeather(cityName)
//        }

        binding.lifecycleOwner = this
        binding.viewModel = model
    }

    fun observe() {

        model.runInProgress.observe(this) {
            binding.progressBar.isVisible = it
        }

        model.errorMessage.observe(this) {
            binding.tvError.text = it
            binding.tvError.isVisible = it.isNotBlank()
        }

        model.weather.observe(this) {
            binding.tvData.text = "Il fait ${it?.main?.temp ?: "-"}° à ${it?.name ?: "-"} avec un vent de ${
                it?.wind?.speed ?: "-"
            } m/s"

            it?.weather?.getOrNull(0)?.icon?.let {
                Picasso.get().load("https://openweathermap.org/img/wn/$it@4x.png")
                    .into(binding.imageView)
            } ?: run {
                binding.imageView.setImageDrawable(null)
            }
        }

    }
}