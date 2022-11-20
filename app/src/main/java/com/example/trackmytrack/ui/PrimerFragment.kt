package com.example.trackmytrack.ui

import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.trackmytrack.BuildConfig
import com.example.trackmytrack.MainActivity
import com.example.trackmytrack.MyApp
import com.example.trackmytrack.R
import com.example.trackmytrack.databinding.FragmentPrimerBinding
import com.example.trackmytrack.utils.*
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar


class PrimerFragment : Fragment() {
    private val TAG = PrimerFragment::class.java.simpleName
//    val viewModel: MainViewModel by viewModels({ requireActivity() })
    val viewModel by activityViewModels<MainViewModel> {
    MainViewModel.MainViewModelFactory(
        (requireContext().applicationContext as MyApp).repository
    )
    }
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

        if((requireActivity() as MainActivity).sharedPreference.getBoolean(IN_ACTION_KEY, false))
            viewModel.inAction.value = true


        /**Views**/
        binding.apply {

            btnForeground.setOnClickListener {
                if(requireContext().isForegroundLocationPermissionsGranted())
                    return@setOnClickListener

                foregroundPermissionResult.launch(getForegroundPermissionsArray())
            }

            btnBackground.setOnClickListener {
                if(requireContext().isBackgroundLocationPermissionsGranted()){
                    viewModel.enableBackground()
                    return@setOnClickListener
                }
                backgroundPermissionResult.launch(getBackgroundPermissionArray())
            }

            btnStartStop.setOnClickListener {
                Log.e("btnStartStop", "Hereee")
                if(requireContext().isBackgroundLocationPermissionsGranted() && requireContext().isForegroundLocationPermissionsGranted())
                {//TODO problem in button activation
                    //TODO check device location enablement
                    viewModel.allNeedsAreGranted()
                    checkDeviceLocationSettingsThenStartGeofence()

                    if(viewModel.inAction.value!!) {
                        // TODO stop geofence process
                    }
                    else {

                    }

                    return@setOnClickListener
                }
                else    //TODO check device location enablement
                    checkDeviceLocationSettingsThenStartGeofence()
            }
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
        } else {
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
        } else {
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

    // A PendingIntent for the Broadcast Receiver that handles geofence transitions.
    /*private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        try {
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        catch (_:Exception){
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }*/

    /**Check Device Location Settings Then Start Geofence**/
    private fun checkDeviceLocationSettingsThenStartGeofence(resolve:Boolean = true) {

        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(LocationManagerCompat.isLocationEnabled(locationManager))
            return

        /**  1- Get some info about the device-location */
        /*val locationRequest : LocationRequest.Builder = LocationRequest.Builder(5, Priority.PRIORITY_HIGH_ACCURACY)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(locationFastestInterval)
            .setMaxUpdateDelayMillis(locationMaxWaitTime)
            .build()*/
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

        /**  2- Check the device-location => If it is disabled, do a loop (dialog ⇄ snakebar) */
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve){
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                    startIntentSenderForResult(exception.resolution.intentSender, REQUEST_TURN_DEVICE_LOCATION_ON, null, 0,0,0, null)        // can work
                }
                catch (sendEx: SendIntentException) {
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else
                Snackbar.make(binding.root, R.string.device_location_required_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.enable) {
                        startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    }.show()
        }

        /**  3- Check the device-location => If it is enabled, do ✅  */
        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                Log.e(TAG, "Heree")
                // todo addGeofence(reminderData, _viewModel.circularRadius.value ?: 100f)
            }
        }
    }

    // When we get the result from asking the user to turn on device location,
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
            checkDeviceLocationSettingsThenStartGeofence(false)
        }
    }

}