package com.example.trackmytrack.data.local

import com.example.trackmytrack.data.Record
import com.example.trackmytrack.data.MainDataSource
import com.example.trackmytrack.utils.ActualResult

class LocalDataSource(private val recordDatabaseDao: RecordersDAO) : MainDataSource {

    override suspend fun getRecords(): ActualResult<List<Record>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecord(record: Record) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRecords() {
        TODO("Not yet implemented")
    }
}