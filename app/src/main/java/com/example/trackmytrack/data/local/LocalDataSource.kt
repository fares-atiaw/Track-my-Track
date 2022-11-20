package com.example.trackmytrack.data.local

import androidx.lifecycle.LiveData
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.data.MainDataSource
import com.example.trackmytrack.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource(private val recordDatabaseDao: RecordersDAO) : MainDataSource {

    override fun getRecords(): Response<LiveData<List<Record>>> {
        return try {
            Response.Success(recordDatabaseDao.getRecords())
        }
        catch (e: Exception) {
            Response.Error(e.message ?: "Nothing to say")
        }
    }

    override suspend fun saveRecord(record: Record) {
        withContext(Dispatchers.IO) {
            recordDatabaseDao.saveRecord(record)
        }
    }

    override suspend fun updateRecord(record: Record) {
        withContext(Dispatchers.IO) {
            recordDatabaseDao.upsertRecord(record)
        }
    }

    override suspend fun deleteAllRecords() {
        withContext(Dispatchers.IO) {
            recordDatabaseDao.deleteAllRecords()
        }
    }
}