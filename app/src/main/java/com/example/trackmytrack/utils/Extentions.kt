package com.example.trackmytrack.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isForegroundLocationPermissionsGranted(): Boolean {
    // Check the 3 permissions
    getForegroundPermissionsArray().forEach {
        if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED)
            return false
    }
    // Here all granted
    return true
}

fun Context.isBackgroundLocationPermissionsGranted(): Boolean {
    // Check the 3 permissions
    getForegroundPermissionsArray().forEach {
        if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED)
            return false
    }
    // Here all granted
    return true
}
