package com.example.trackmytrack.repository

import com.example.trackmytrack.data.Record

interface MainDataSource {

    suspend fun getRecords(): List<Record>
    suspend fun saveRecord(record: Record)
    suspend fun deleteAllRecords()
}