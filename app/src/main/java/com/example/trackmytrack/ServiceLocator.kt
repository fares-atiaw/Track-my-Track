package com.example.trackmytrack

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.trackmytrack.data.local.LocalDataSource
import com.example.trackmytrack.data.local.RecordDatabase
import com.example.trackmytrack.data.MainDataSource
import com.example.trackmytrack.repository.DefaultRecordsRepository
import com.example.trackmytrack.repository.DefaultRepository

object ServiceLocator {

    private var database: RecordDatabase? = null
    @Volatile
    var tasksRepository: DefaultRepository? = null
        @VisibleForTesting set      // The setter here is just for testing cases, but of course not for the real code.

    private val lock = Any()

    fun provideTasksRepository(context: Context): DefaultRepository {
        synchronized(this) {
            return tasksRepository ?: createDefaultRecordsRepository(context)
        }
    }

    private fun createDefaultRecordsRepository(context: Context): DefaultRepository {
        val newRepo = DefaultRecordsRepository(getLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun getLocalDataSource(context: Context): MainDataSource {
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.recordDatabaseDao())
    }

    private fun createDataBase(context: Context): RecordDatabase {
        synchronized(this)         // Wrapping with ‘synchronized(context){}’ means that only one thread of execution can enter its block.
        {
            if (database == null) {
                database = Room.databaseBuilder(
                    context.applicationContext,
                    RecordDatabase::class.java,
                    "location_records_database"    // If it not exist, it will create it.
                )
                    .fallbackToDestructiveMigration()    // handle versions
                    .build()
            }
            return database as RecordDatabase
        }
    }


    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }
    }
}