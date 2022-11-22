package com.example.trackmytrack.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trackmytrack.repository.DefaultRepository
import com.example.trackmytrack.utils.Response
import java.util.LinkedHashMap

/** Using FakeDataSource with static access to the data for easy testing. **/
class FakeDataSource : DefaultRepository {

    private var _recordsList = MutableLiveData<List<Record>>(emptyList())
    val recordsList: LiveData<List<Record>>
        get() = _recordsList

    var lista: LinkedHashMap<Int, Record> = LinkedHashMap()

    private var shouldReturnError = false

    override fun getRecords(): Response<LiveData<List<Record>>> {
        if (shouldReturnError) {
            return Response.Error("Error happened")
        }
        recordsList.let {
            return Response.Success(it)
        }
    }

    override suspend fun saveRecord(record: Record) {
        lista[record.id] = record
    }

    override suspend fun updateRecord(record: Record) {
//        TODO ==> Not being used
    }

    override suspend fun deleteAllRecords() {
        lista.clear()
    }


}