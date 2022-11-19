package com.example.trackmytrack.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isLocationPermissionsGranted(): Boolean {
    // Check the 3 permissions
    getPermissionsArray().forEach {
        if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED)
            return false
    }
    // Here all granted
    return true
}