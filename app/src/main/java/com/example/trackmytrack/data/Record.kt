package com.example.trackmytrack.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_record_table")
data class Record(
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "place") val place: String?,
    @PrimaryKey
    @ColumnInfo(name = "time_from") val timeFrom: String?,
    @ColumnInfo(name = "time_to") val timeTo: String?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
)