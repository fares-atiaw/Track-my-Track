package com.example.trackmytrack.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.trackmytrack.MyApp
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.utils.KEY_IN_ACTION
import com.example.trackmytrack.utils.KEY_METERS_RECORDED
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class RecordingService : LocationListener, Service() {

    lateinit var locationManager : LocationManager
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var meters = 0

    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var current : LocalDateTime
    @RequiresApi(Build.VERSION_CODES.O)
    val dayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    @RequiresApi(Build.VERSION_CODES.O)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var currentDay : String
    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var currentTime: String

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        locationManager = baseContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0F, this)

        sharedPreference = baseContext.getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPreference.edit()
        editor.putInt(KEY_METERS_RECORDED, meters)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationChanged(location: Location) {
        current = LocalDateTime.now()
        currentDay = LocalDateTime.now().format(dayFormatter)
        currentTime = LocalDateTime.now().format(timeFormatter)
        meters += 5
        editor.putInt(KEY_METERS_RECORDED, meters)

        Log.e("onLocationChanged", "location.latitude ${currentDay} && location.longitude ${currentTime}")
        Log.e("onLocationChanged", "location.latitude ${location.latitude} && location.longitude ${location.longitude}")
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        editor.putBoolean(KEY_IN_ACTION, false)
        Toast.makeText(baseContext, "The recording process has stopped, as the location settings has been disabled", Toast.LENGTH_LONG).show()
        locationManager.removeUpdates(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        editor.putBoolean(KEY_IN_ACTION, false)
        locationManager.removeUpdates(this)
    }

}