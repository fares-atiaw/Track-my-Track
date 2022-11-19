package com.example.trackmytrack.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

/**Variables**/
val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

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