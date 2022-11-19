package com.example.trackmytrack.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.trackmytrack.BuildConfig
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
            Log.e("btnForeground", "Hereee")
            if(requireContext().isForegroundLocationPermissionsGranted())
                return@setOnClickListener

            foregroundPermissionResult.launch(getForegroundPermissionsArray())
        }

        binding.btnBackground.setOnClickListener {
            Log.e("btnBackground", "Hereee")
            if(requireContext().isBackgroundLocationPermissionsGranted()){
                viewModel.enableBackground()
                return@setOnClickListener
            }

            backgroundPermissionResult.launch(getBackgroundPermissionArray())
        }



        return binding.root
    }


    private val foregroundPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // After the user choose from the Permission-Dialog ↴
        if (permissions.values.all { it }) {
            // Now all the permissions are granted
            viewModel.enableForeground()
            //todo check device location enablement
            Log.e("foregroundResult", "Heree0")
        } else {
            Log.e("foregroundResult", "Heree1")
            //todo snakebar
            Snackbar.make(binding.root, R.string.foreground_permission_required_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }
                .show()
        }
    }

    private val backgroundPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // After the user choose from the Permission-Dialog ↴
        if (permissions.values.all { it }) {
            // Now all the permissions are granted
            viewModel.enableBackground()
            Log.e("backgroundResult", "Heree0")
            //todo check device location enablement
        } else {
            Log.e("backgroundResult", "Heree1")
            //todo snakebar
            Snackbar.make(binding.root, R.string.background_permission_required_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }
                .show()
        }
    }


}