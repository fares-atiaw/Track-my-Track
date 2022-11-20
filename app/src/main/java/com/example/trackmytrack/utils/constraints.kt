package com.example.trackmytrack.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

/**Variables**/
val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
const val REQUEST_TURN_DEVICE_LOCATION_ON = 11
const val IN_ACTION_KEY : String = "in_action"

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