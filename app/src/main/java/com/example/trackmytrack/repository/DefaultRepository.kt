package com.example.trackmytrack.repository

import com.example.trackmytrack.data.Record
import com.example.trackmytrack.utils.ActualResult

interface DefaultRepository {
    suspend fun getRecords(): ActualResult<List<Record>>
    suspend fun saveRecord(record: Record)
    suspend fun deleteAllRecords()
}