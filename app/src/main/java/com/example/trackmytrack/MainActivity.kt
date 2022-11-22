package com.example.trackmytrack

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.trackmytrack.utils.KEY_IN_ACTION
import com.example.trackmytrack.utils.KEY_METERS_RECORDED

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        editor = sharedPreference.edit()
        // for the first time only
        if(!sharedPreference.contains(KEY_IN_ACTION)) {
            Log.e("IN_ACTION_KEY", KEY_IN_ACTION)
            editor.putBoolean(KEY_IN_ACTION, false)
            editor.putInt(KEY_METERS_RECORDED, 0)
            editor.commit()
        }

    }
}