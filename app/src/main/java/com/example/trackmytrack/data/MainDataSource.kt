package com.example.trackmytrack.data

import androidx.lifecycle.LiveData
import com.example.trackmytrack.utils.Response

interface MainDataSource {

    fun getRecords(): Response<LiveData<List<Record>>>
    suspend fun saveRecord(record: Record)
    suspend fun updateRecord(record: Record)
    suspend fun deleteAllRecords()
}