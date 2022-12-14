package com.example.trackmytrack.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "location_record_table")
data class Record(
    var date: String?,
    var place: String?,
    val time: String,
    var latitude: Double?,
    var longitude: Double?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
) : Parcelable
