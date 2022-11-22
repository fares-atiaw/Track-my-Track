package com.example.trackmytrack.ui

import androidx.lifecycle.*
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.repository.DefaultRepository
import com.example.trackmytrack.utils.Response
import kotlinx.coroutines.launch

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

    var inAction = MutableLiveData<Boolean>(false)

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
    val allTrackRecordsList : Response<LiveData<List<Record>>> by lazy {
        repo.getRecords()
    }

    val date = MutableLiveData<String?>()
    val place = MutableLiveData<String?>()
    val timeFrom = MutableLiveData<String?>()
    val timeTo = MutableLiveData<String?>()
    val latitude = MutableLiveData<Double?>()
    val longitude = MutableLiveData<Double?>()


    //System.currentTimeMillis()

    /**related methods**/
    fun saveRecord(dataRecord: Record) {
        viewModelScope.launch {
            repo.saveRecord(dataRecord)
        }
    }

    fun clearRecords() {
        viewModelScope.launch {
            repo.deleteAllRecords()
        }
    }



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