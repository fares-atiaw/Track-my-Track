package com.example.trackmytrack

import android.app.Application
import com.example.trackmytrack.repository.DefaultRepository

class MyApp : Application() {

    val repository: DefaultRepository
        get() = ServiceLocator.provideTasksRepository(this)

}