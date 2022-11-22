package com.example.trackmytrack.utils

import android.Manifest
import android.os.Build
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import com.example.trackmytrack.SpecificWork
import java.util.concurrent.TimeUnit

/**Variables**/
val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
const val REQUEST_TURN_DEVICE_LOCATION_ON = 11
const val KEY_IN_ACTION : String = "in_action"
const val KEY_METERS_RECORDED : String = "meters"

/**Functions**/
fun getForegroundPermissionsArray(): Array<String>
{
    val permissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    return permissions.toTypedArray()   // for a certain usage later
}

fun getBackgroundPermissionArray(): Array<String>
{
    val permissions = mutableListOf(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )
    return permissions.toTypedArray()   // for a certain usage later
}