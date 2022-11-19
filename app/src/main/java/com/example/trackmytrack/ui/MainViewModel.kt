package com.example.trackmytrack.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val foregroundEnabled = MutableLiveData<Boolean>(false)
    val backgroundEnabled = MutableLiveData<Boolean>(false)

    fun enableForeground() {
        foregroundEnabled.postValue(true)
    }

    fun enableBackground() {
        backgroundEnabled.postValue(true)
    }

}