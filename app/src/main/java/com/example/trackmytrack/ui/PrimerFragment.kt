package com.example.trackmytrack.ui

import android.content.Intent
import android.content.IntentSender
import android.location.LocationRequest
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.trackmytrack.R
import com.example.trackmytrack.databinding.FragmentPrimerBinding
import com.google.android.material.snackbar.Snackbar

class PrimerFragment : Fragment() {
    private val TAG = PrimerFragment::class.java.simpleName
    val viewModel: MainViewModel by viewModels({ requireActivity() })
    private lateinit var binding : FragmentPrimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPrimerBinding.inflate(inflater, container, false)

        binding.data = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }


    private val resolutionForResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // After the user choose from the Permission-Dialog ↴
        if (permissions.values.all { it }) {
            // Now all the 3 permissions are granted
//            checkDeviceLocationSettingsThenStartGeofence()
        } else {
            //todo snakebar
        }
    }


}