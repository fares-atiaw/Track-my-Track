package com.example.trackmytrack.repository

import androidx.lifecycle.LiveData
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.utils.Response

interface DefaultRepository {
    fun getRecords(): Response<LiveData<List<Record>>>
    suspend fun saveRecord(record: Record)
    suspend fun updateRecord(record: Record)
    suspend fun deleteAllRecords()
}