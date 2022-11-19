package com.example.trackmytrack.utils

import android.graphics.Color
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.example.trackmytrack.R

@BindingAdapter("android:checkEnablement")
fun checkIfEnabled(btn: Button, flag: Boolean)
{
    if(flag) {
        btn.isEnabled = false
        btn.text = "Enabled"
        btn.setBackgroundColor(btn.resources.getColor(R.color.custom_green))
    }
    else{
        btn.isEnabled = true
        btn.text = "Enable"
        btn.setBackgroundColor(btn.resources.getColor(R.color.custom_red))    //btn.resources.getColor(R.color.custom_red)
//        btn.setBackgroundColor(Color.parseColor("950505"))    //btn.resources.getColor(R.color.custom_red)
    }
}
