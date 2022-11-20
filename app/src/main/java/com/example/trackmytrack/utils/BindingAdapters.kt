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
        btn.isActivated = false
        btn.text = btn.resources.getString(R.string.enabled)
        btn.setBackgroundColor(btn.resources.getColor(R.color.custom_green))
    }
    else{
        btn.isEnabled = true
        btn.isActivated = true
        btn.text = btn.resources.getString(R.string.not_enabled)
        btn.setBackgroundColor(btn.resources.getColor(R.color.custom_red))    //btn.resources.getColor(R.color.custom_red)
//        btn.setBackgroundColor(Color.parseColor("950505"))    //btn.resources.getColor(R.color.custom_red)
    }
}

@BindingAdapter("android:checkForAction")
fun checkForActions(btn: Button, flag: Boolean)
{
//        btn.isEnabled = !flag
//        btn.isActivated = !flag
}
