package com.example.trackmytrack.utils

import android.graphics.Color
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.example.trackmytrack.R

object BindingAdapters {

    @BindingAdapter("android:checkEnablement")
    fun checkEnablement(btn: Button, flag: Boolean)
    {
        if(flag) {
            btn.isEnabled = true
            btn.text = "Enabled"
            btn.setBackgroundColor(Color.GREEN)
        }
        else{
            btn.isEnabled = false
            btn.text = "Enable"
            btn.setBackgroundColor(Color.parseColor("950505"))    //btn.resources.getColor(R.color.custom_red)
        }
    }



}