package com.example.trackmytrack.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun getPermissionsArray(): Array<String> {
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

fun Context.isLocationPermissionsGranted(): Boolean {
    // Check the 3 permissions
    getPermissionsArray().forEach {
        if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED)
            return false
    }
    // Here all granted
    return true
}