package com.example.trackmytrack.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_record_table")
data class Record(
    var date: String?,
    var place: String?,
    @PrimaryKey
    @ColumnInfo(name = "time_from") val timeFrom: String,
    @ColumnInfo(name = "time_to") val timeTo: String,
    var latitude: Double?,
    var longitude: Double?,
)
