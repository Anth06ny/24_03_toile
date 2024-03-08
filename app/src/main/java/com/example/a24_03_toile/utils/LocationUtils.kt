package com.example.a24_03_toile.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task

object LocationUtils {

    //Récupère la dernière localisation
    //Asynchrone : Déclenche la puce GPS si la dernière localisation est vielle
    fun getLastKnowLocationEconomyMode(context: Context): Task<Location>? {

        //si on n'a pas la permission
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return null
        }

        return LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)

//            .addOnSuccessListener (onLocationReceived) //J'ai une localisation
//            .addOnFailureListener {
//                it.printStackTrace()
//                println("Erreur de localisation : ${it.message}")
//            }
    }

}