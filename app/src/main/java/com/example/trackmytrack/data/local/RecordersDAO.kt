package com.example.trackmytrack.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.trackmytrack.data.Record

@Dao
interface RecordersDAO {

    // Get all records.
    @Query("SELECT * FROM location_record_table")
    fun getRecords(): LiveData<List<Record>>

    // Add record
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveRecord(record: Record)

    // Update time_to of record
    @Update
    suspend fun upsertRecord(data: Record)

    // Delete all records.
    @Query("DELETE FROM location_record_table")
    suspend fun deleteAllRecords()

}