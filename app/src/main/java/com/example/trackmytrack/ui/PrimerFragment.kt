package com.example.trackmytrack.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trackmytrack.BuildConfig
import com.example.trackmytrack.MainActivity
import com.example.trackmytrack.MyApp
import com.example.trackmytrack.R
import com.example.trackmytrack.data.Record
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
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreference = requireContext().getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPreference.edit()

        /**Check at first**/
        if(requireContext().isForegroundLocationPermissionsGranted())
            viewModel.enableForeground()

        if(requireContext().isBackgroundLocationPermissionsGranted())
            viewModel.enableBackground()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentPrimerBinding.inflate(inflater, container, false)

        binding.data = viewModel
        binding.lifecycleOwner = this

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

            btnSeeList.setOnClickListener {
                findNavController().navigate(R.id.action_primerFragment_to_recordedLocationsFragment)
            }

            btnStartStop.setOnClickListener {
                if(requireContext().isBackgroundLocationPermissionsGranted() && requireContext().isForegroundLocationPermissionsGranted()) {
                    //TODO problem in button activation
                    viewModel.allNeedsAreGranted()

                    if(viewModel.inAction.value!!) // already running
                        stopProcesses()
                    else // start a new one
                        checkDeviceLocationSettingsThenStartGeofence()
//                        startProcesses(record)

                    return@setOnClickListener
                }
                else    //TODO check device location enablement
                    Snackbar.make(binding.root, R.string.both_permissions_required_error, Snackbar.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun stopProcesses() {
        viewModel.inAction.value = false
        editor.putBoolean(IN_ACTION_KEY, false)
        // TODO stop geofence process
    }

    private fun startProcesses(record: Record) {
        //TODO check device location enablement
        // TODO add current data to the ViewModel, and from there you can save the record
        checkDeviceLocationSettingsThenStartGeofence()
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

    /**Check Device Location Settings Then Start Geofence**/
    private fun checkDeviceLocationSettingsThenStartGeofence()
    {
        /**  At first  **/
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(LocationManagerCompat.isLocationEnabled(locationManager)){
            // todo get the required needs
            viewModel.inAction.value = true
            editor.putBoolean(IN_ACTION_KEY, true)

            return
        }

        /**  1- Get some info about the device-location */
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500)
            .setMaxUpdateDelayMillis(1000)
            .build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true)
        val settingsClient = LocationServices.getSettingsClient(requireContext())
        val locationSettingsResponse = settingsClient.checkLocationSettings(builder.build())

        /**  2- Check the device-location => If it is disabled, show a snakebar */
        locationSettingsResponse.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
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
        locationSettingsResponse.addOnCompleteListener {
            if ( it.isSuccessful ) {
                Log.e(TAG, "isSuccessful")
                // todo get the required needs
                viewModel.inAction.value = true
                editor.putBoolean(IN_ACTION_KEY, true)
            }
        }
    }

    // When we get the result from asking the user to turn on device location,
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
            when (resultCode) {
                Activity.RESULT_OK ->
                    checkDeviceLocationSettingsThenStartGeofence()
                Activity.RESULT_CANCELED ->
                    Snackbar.make(binding.root, R.string.device_location_required_error, Snackbar.LENGTH_SHORT).show()
            }
        }
// Toast.makeText(requireContext(), "GPS is turned on", Toast.LENGTH_SHORT).show()
    }
}






/*LocationServices.getSettingsClient(requireActivity())
    .checkLocationSettings(locationRequestBuilder.build())
    .addOnCompleteListener {
        try {
            it.getResult(ApiException::class.java)
            // Location settings are On
        } catch (exception: ApiException) { // Location settings are Off
            when (exception.statusCode) {
                RESOLUTION_REQUIRED -> try { // Check result in onActivityResult
                    val resolvable = exception as ResolvableApiException
                    resolvable.startResolutionForResult(requireActivity(), LOCATION_REQUEST_CODE)
                } catch (ignored: IntentSender.SendIntentException) {
                } catch (ignored: ClassCastException) {
                }
                // Location settings are not available on device
            }
        }
    }*/

/**  1- Get some info about the device-location */
/*val locationRequest : LocationRequest.Builder = LocationRequest.Builder(5, Priority.PRIORITY_HIGH_ACCURACY)
    .setWaitForAccurateLocation(false)
    .setMinUpdateIntervalMillis(locationFastestInterval)
    .setMaxUpdateDelayMillis(locationMaxWaitTime)
    .build()*/
/*val locationRequest = LocationRequest.create().apply {
    interval = 100
    fastestInterval = 50
    priority = Priority.PRIORITY_HIGH_ACCURACY
    maxWaitTime = 100
}
val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
val settingsClient = LocationServices.getSettingsClient(requireActivity())
val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())*/

/**  2- Check the device-location => If it is disabled, do a loop (dialog ⇄ snakebar) */
/*locationSettingsResponseTask.addOnFailureListener { exception ->
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
        Log.e(TAG, "isSuccessful")
        viewModel.inAction.value = true
        editor.putBoolean(IN_ACTION_KEY, true)
    }
}*/