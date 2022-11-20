package com.example.trackmytrack.repository

import androidx.lifecycle.LiveData
import com.example.trackmytrack.data.MainDataSource
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.utils.Response

class DefaultRecordsRepository(private val localDataSource: MainDataSource) : DefaultRepository
{
    override fun getRecords(): Response<LiveData<List<Record>>> =
        localDataSource.getRecords()

    override suspend fun saveRecord(record: Record) {
        localDataSource.saveRecord(record)
    }

    override suspend fun updateRecord(record: Record) {
        localDataSource.updateRecord(record)
    }

    override suspend fun deleteAllRecords() {
        localDataSource.deleteAllRecords()
    }
}