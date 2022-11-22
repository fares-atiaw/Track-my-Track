package com.example.trackmytrack.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.trackmytrack.MyApp
import com.example.trackmytrack.data.Record
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyLocationReceiver : BroadcastReceiver() {

    lateinit var locationResult : LocationResult

    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var current : LocalDateTime
    @RequiresApi(Build.VERSION_CODES.O)
    val dayFormatter = DateTimeFormatter.ofPattern("dd-MM")
    @RequiresApi(Build.VERSION_CODES.O)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var currentDay : String
    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var currentTime: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(c: Context?, i: Intent?) {

        current = LocalDateTime.now()
        currentDay = LocalDateTime.now().format(dayFormatter)
        currentTime = LocalDateTime.now().format(timeFormatter)

        if(i != null) {
            locationResult = LocationResult.extractResult(i)!!
            val location = locationResult.lastLocation

            val data = Record(currentDay, null, currentTime, location?.latitude, location?.longitude)
            CoroutineScope(Dispatchers.IO).launch {
                MyApp().repository.saveRecord(data)
            }
        }

    }

}