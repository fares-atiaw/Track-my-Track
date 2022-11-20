package com.example.trackmytrack

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.trackmytrack.utils.IN_ACTION_KEY

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        editor = sharedPreference.edit()
        // for the first time only
        if(!sharedPreference.contains(IN_ACTION_KEY)) {
            Log.e("IN_ACTION_KEY", IN_ACTION_KEY)
            editor.putBoolean(IN_ACTION_KEY, false)
            editor.commit()
        }

    }
}