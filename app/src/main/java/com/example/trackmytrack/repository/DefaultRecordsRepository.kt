package com.example.trackmytrack.repository

import com.example.trackmytrack.data.MainDataSource
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.utils.ActualResult

class DefaultRecordsRepository(private val localDataSource: MainDataSource) : DefaultRepository
{
    override suspend fun getRecords(): ActualResult<List<Record>> =
        localDataSource.getRecords()

    override suspend fun saveRecord(record: Record) {
        localDataSource.saveRecord(record)
    }

    override suspend fun deleteAllRecords() {
        localDataSource.deleteAllRecords()
    }
}