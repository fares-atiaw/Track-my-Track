package com.example.trackmytrack.utils

import android.Manifest
import android.os.Build

/**Variables**/
val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

/**Functions**/
fun getPermissionsArray(): Array<String>
{
    // Foreground => ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION permissions
    val permissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    // Background => ACCESS_BACKGROUND_LOCATION(on Android 10+ (Q)) permission
    if (runningQOrLater)
        permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    return permissions.toTypedArray()   // for a certain usage later
}