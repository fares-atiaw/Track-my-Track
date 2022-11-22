package com.example.trackmytrack.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.trackmytrack.BuildConfig
import com.example.trackmytrack.R
import com.google.android.material.snackbar.Snackbar

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
