package com.example.trackmytrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackmytrack.data.Record

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordDatabase : RoomDatabase()  {

    // Room will make the connection details with the Dao file, so you will call the Dao functions using this.
    abstract fun recordDatabaseDao(): RecordersDAO

}


/*companion object {

    @Volatile	// It means that it will never be cached, and it will execute in the main memory (No intersection calls).
    private var INSTANCE: RecordDatabase? = null

    fun getInstance(context: Context): RecordDatabase
    {
        synchronized(this) {		// Wrapping with ‘synchronized(context){}’ means that only one thread of execution can enter its block.
            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordDatabase::class.java,
                    "location_records_database"    // If it not exist, it will create it.
                )
                    .fallbackToDestructiveMigration()	// handle versions
                    .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}*/