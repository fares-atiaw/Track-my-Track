package com.example.trackmytrack.repository

import com.example.trackmytrack.data.MainDataSource
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.utils.ActualResult

class DefaultRecordsRepository(localDataSource: MainDataSource) : DefaultRepository
{
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