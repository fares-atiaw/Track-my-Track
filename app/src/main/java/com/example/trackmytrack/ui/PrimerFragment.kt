package com.example.trackmytrack.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.PendingIntent
import android.content.*
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trackmytrack.*
import com.example.trackmytrack.R
import com.example.trackmytrack.component.MyLocationReceiver
import com.example.trackmytrack.data.Record
import com.example.trackmytrack.databinding.FragmentPrimerBinding
import com.example.trackmytrack.component.RecordingService
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
    lateinit var locationManager: LocationManager
    lateinit var locationRequest : LocationRequest
    lateinit var focusedLocationProviderClient : FusedLocationProviderClient
    val locationPermissionCode = 2
    lateinit var intentService : Intent
    var meters = 0
    private lateinit var dialog: Dialog

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, i: Intent?) {
            val data = i?.getParcelableExtra<Record>("data")
            viewModel.saveRecord(data!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreference = requireContext().getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPreference.edit()

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        intentService = Intent(context, RecordingService::class.java)
        dialog = Dialog(requireContext())

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
        binding.lifecycleOwner = viewLifecycleOwner

        if((requireActivity() as MainActivity).sharedPreference.getBoolean(KEY_IN_ACTION, false))
            viewModel.inAction.value = true
        // I couldn't know why it's not true ????

        viewModel.allTrackRecordsList.data?.observe(viewLifecycleOwner) { result ->
            meters = result.size * 5
        }

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
                        startProcesses()
                }
                else
                    Snackbar.make(binding.root, R.string.both_permissions_required_error, Snackbar.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.apply {
            setContentView(R.layout.custom_final_dialog)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.window?.setBackgroundDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.dialog_design
                    )
                )
            }
            window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.attributes?.windowAnimations = R.style.DialogAnimation

            setCancelable(true)
        }
    }

    private fun stopProcesses() {
        viewModel.inAction.value = false
        editor.putBoolean(KEY_IN_ACTION, false)

        requireContext().stopService(intentService)
        requireActivity().unregisterReceiver(receiver)

        dialog.findViewById<TextView>(R.id.tv_final_message).text = "You have crossed $meters meters"
        dialog.show()
        viewModel.clearRecords()
    }

    @SuppressLint("MissingPermission")
    private fun startProcesses() {
        if(checkDeviceLocationSettingsThenStartWork()) {
            viewModel.inAction.value = true
            editor.putBoolean(KEY_IN_ACTION, true)

//            focusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//            focusedLocationProviderClient.requestLocationUpdates(locationRequest, receiverPendingIntent)

            requireContext().startService(intentService)
            requireActivity().registerReceiver(receiver, IntentFilter("location_update"))
        }
    }

//    private val receiverPendingIntent: PendingIntent by lazy {
//        val intentReceiver = Intent(context, MyLocationReceiver::class.java)
//        PendingIntent.getBroadcast(context, 0, intentReceiver, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
//    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500)
            .setMaxUpdateDelayMillis(1000)
            .build()
    }

/**Control permissions**/
    private val foregroundPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // After the user choose from the Permission-Dialog ???
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
        // After the user choose from the Permission-Dialog ???
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
    private fun checkDeviceLocationSettingsThenStartWork() : Boolean
    {
        buildLocationRequest()

        /**  At first  **/
        if(LocationManagerCompat.isLocationEnabled(locationManager))
            return true

        /**  1- Get some info about the device-location */
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true)
        val settingsClient = LocationServices.getSettingsClient(requireContext())
        val locationSettingsResponse = settingsClient.checkLocationSettings(builder.build())

        /**  2- Check the device-location => If it is disabled, show a snakebar and return false */
        var flag = false
        locationSettingsResponse.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                try {
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
            flag = false
        }

        /**  3- Check the device-location => If it is enabled, return true */
        locationSettingsResponse.addOnCompleteListener {
            if ( it.isSuccessful )
                flag = true
        }

        return flag
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
        viewModel.clearRecords()
    }

    // When we get the result from asking the user to turn on device location,
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
            when (resultCode) {
                Activity.RESULT_OK ->
                    checkDeviceLocationSettingsThenStartWork()
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

/**  2- Check the device-location => If it is disabled, do a loop (dialog ??? snakebar) */
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

/**  3- Check the device-location => If it is enabled, do ???  */
locationSettingsResponseTask.addOnCompleteListener {
    if ( it.isSuccessful ) {
        Log.e(TAG, "isSuccessful")
        viewModel.inAction.value = true
        editor.putBoolean(IN_ACTION_KEY, true)
    }
}*/