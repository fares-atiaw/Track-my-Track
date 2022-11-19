package com.example.trackmytrack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var _foregroundEnabled = MutableLiveData<Boolean>(false)
    val foregroundEnabled : LiveData<Boolean>
        get() = _foregroundEnabled

    private var _backgroundEnabled = MutableLiveData<Boolean>(false)
    val backgroundEnabled : LiveData<Boolean>
        get() = _backgroundEnabled

    private var _allGranted = MutableLiveData<Boolean>(false)
    val allGranted : LiveData<Boolean>
        get() = _allGranted

    fun enableForeground() {
        _foregroundEnabled.postValue(true)
    }

    fun enableBackground() {
        _backgroundEnabled.postValue(true)
    }

    fun allNeedsAreGranted() {
        _allGranted.postValue(true)
    }
//    fun disableStartStop() {
//        _allGranted.postValue(false)
//    }


}