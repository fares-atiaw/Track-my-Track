package com.example.trackmytrack.data.local

import com.example.trackmytrack.data.Record
import com.example.trackmytrack.data.MainDataSource
import com.example.trackmytrack.utils.ActualResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource(private val recordDatabaseDao: RecordersDAO) : MainDataSource {

    override suspend fun getRecords(): ActualResult<List<Record>> =
        withContext(Dispatchers.IO) {
        return@withContext try {
            ActualResult.Success(recordDatabaseDao.getRecords())
        }
        catch (e: Exception) {
            ActualResult.Error(e.message ?: "Nothing to say")
        }
    }

    override suspend fun saveRecord(record: Record) {
        withContext(Dispatchers.IO) {
            recordDatabaseDao.saveRecord(record)
        }
    }

    override suspend fun deleteAllRecords() {
        withContext(Dispatchers.IO) {
            recordDatabaseDao.deleteAllRecords()
        }
    }
}