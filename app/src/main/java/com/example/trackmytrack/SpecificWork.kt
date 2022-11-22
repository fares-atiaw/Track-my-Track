package com.example.trackmytrack

import android.content.Context
import android.location.Location
import android.location.LocationListener
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.util.Log
import com.example.trackmytrack.data.Record

class SpecificWork(appContext: Context, params: WorkerParameters): LocationListener, CoroutineWorker(appContext, params) {
    // TODO("Get Location and time and date")

    /*private fun getCurrentRecord() : Record
    {

    }

    private fun saveCurrentRecord(record : Record)
    {

    }*/

    override suspend fun doWork(): Result {
        return try {
            Log.e("TODO TODO", "Get Location and time and date")
            Result.success()
        } catch (e: Exception){
            Result.retry()
        }
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }


}