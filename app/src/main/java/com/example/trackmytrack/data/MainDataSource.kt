package com.example.trackmytrack.data

import com.example.trackmytrack.utils.ActualResult

interface MainDataSource {

    suspend fun getRecords(): ActualResult<List<Record>>
    suspend fun saveRecord(record: Record)
    suspend fun deleteAllRecords()
}