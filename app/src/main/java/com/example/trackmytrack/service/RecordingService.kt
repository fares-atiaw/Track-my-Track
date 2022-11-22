package com.example.trackmytrack.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.trackmytrack.utils.KEY_IN_ACTION
import com.example.trackmytrack.utils.KEY_METERS_RECORDED
import kotlinx.coroutines.*

class RecordingService : LocationListener, Service() {

    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var meters = 0

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        sharedPreference = baseContext.getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPreference.edit()
        editor.putInt(KEY_METERS_RECORDED, meters)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationChanged(location: Location) {
        meters += 5
        editor.putInt(KEY_METERS_RECORDED, meters)

        // TODO here the data will be granted
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        editor.putBoolean(KEY_IN_ACTION, false)
        Toast.makeText(baseContext, "The recording process has stopped, as the location settings has been disabled", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        editor.putBoolean(KEY_IN_ACTION, false)
    }

}