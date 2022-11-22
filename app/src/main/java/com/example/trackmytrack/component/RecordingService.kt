package com.example.trackmytrack.component

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
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.utils.KEY_IN_ACTION
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordingService : LocationListener, Service() {

    lateinit var locationManager : LocationManager
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

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

    override fun onBind(p0: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        locationManager = baseContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5F, this)

        sharedPreference = baseContext.getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPreference.edit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationChanged(location: Location) {
        current = LocalDateTime.now()
        currentDay = LocalDateTime.now().format(dayFormatter)
        currentTime = LocalDateTime.now().format(timeFormatter)

        val data = Record(currentDay, null, currentTime, location.latitude, location.longitude)

        val i = Intent("location_update")

        scope.launch {
            i.putExtra("data", data)
            sendBroadcast(i)
        }

        Log.e("onLocationChanged", "Time $currentTime && location.latitude ${location.latitude} && location.longitude ${location.longitude}")
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        editor.putBoolean(KEY_IN_ACTION, false)
        Toast.makeText(baseContext, "The recording process has stopped, as the location settings has been disabled", Toast.LENGTH_LONG).show()
        locationManager.removeUpdates(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        editor.putBoolean(KEY_IN_ACTION, false)
        locationManager.removeUpdates(this)
    }

}