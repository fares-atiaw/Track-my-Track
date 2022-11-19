package com.example.trackmytrack.ui

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.trackmytrack.R
import com.example.trackmytrack.databinding.FragmentPrimerBinding
import com.example.trackmytrack.utils.*
import com.google.android.material.snackbar.Snackbar

class PrimerFragment : Fragment() {
    private val TAG = PrimerFragment::class.java.simpleName
    val viewModel: MainViewModel by viewModels({ requireActivity() })
    private lateinit var binding : FragmentPrimerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentPrimerBinding.inflate(inflater, container, false)

        binding.data = viewModel
        binding.lifecycleOwner = this

        /**Check at first**/
        if(requireContext().isForegroundLocationPermissionsGranted())
            viewModel.enableForeground()

        if(requireContext().isBackgroundLocationPermissionsGranted())
            viewModel.enableBackground()

        binding.btnForeground.setOnClickListener {
            if(requireContext().isForegroundLocationPermissionsGranted())
                return@setOnClickListener

            resolutionForResult.launch(getForegroundPermissionsArray())
        }

        binding.btnBackground.setOnClickListener {
            if(requireContext().isBackgroundLocationPermissionsGranted())
                return@setOnClickListener

            resolutionForResult.launch(getBackgroundPermissionArray())
        }

        return binding.root
    }


    private val resolutionForResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // After the user choose from the Permission-Dialog ↴
        if (permissions.values.all { it }) {
            // Now all the permissions are granted
            viewModel.enableForeground()
            //todo check device location enablement
        } else {
            //todo snakebar
        }
    }


}