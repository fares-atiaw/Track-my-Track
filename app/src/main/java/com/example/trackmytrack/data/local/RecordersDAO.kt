package com.example.trackmytrack.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackmytrack.data.Record

@Dao
interface RecordersDAO {

    // Get all records.
    @Query("SELECT * FROM location_record_table")
    suspend fun getRecords(): List<Record>

    // Adding a record
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveRecord(record: Record)

    // Delete all records.
    @Query("DELETE FROM location_record_table")
    suspend fun deleteAllRecords()

}