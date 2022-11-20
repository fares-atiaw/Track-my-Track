package com.example.trackmytrack.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackmytrack.repository.DefaultRepository

class MainViewModel(private val repo : DefaultRepository) : ViewModel() {

    /**variables**/
    private var _foregroundEnabled = MutableLiveData<Boolean>(false)
    val foregroundEnabled : LiveData<Boolean>
        get() = _foregroundEnabled

    private var _backgroundEnabled = MutableLiveData<Boolean>(false)
    val backgroundEnabled : LiveData<Boolean>
        get() = _backgroundEnabled

    private var _allGranted = MutableLiveData<Boolean>(false)
    val allGranted : LiveData<Boolean>
        get() = _allGranted

    /**related methods**/
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

    /**variables**/
    // todo get required record's needs


    /**related methods**/




    class MainViewModelFactory(private val repo : DefaultRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}