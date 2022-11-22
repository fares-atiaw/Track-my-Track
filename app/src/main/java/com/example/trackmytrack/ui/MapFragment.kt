package com.example.trackmytrack.ui

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.example.trackmytrack.MyApp
import com.example.trackmytrack.R
import com.example.trackmytrack.data.Record
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapFragment : Fragment(), OnMapReadyCallback {
    private val TAG = MapFragment::class.java.simpleName
    private lateinit var map: GoogleMap
    private var marker: Marker? = null
    val viewModel by activityViewModels<MainViewModel> {
        MainViewModel.MainViewModelFactory(
            (requireContext().applicationContext as MyApp).repository
        )
    }
    lateinit var lista : List<Record>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        viewModel.allTrackRecordsList.data?.observe(viewLifecycleOwner) { result ->
            Log.e("3rd Fragment", result.toString())
            lista = result
        }

        /**Menu Setup**/
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.map_options, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.normal_map -> {
                        map.mapType = GoogleMap.MAP_TYPE_NORMAL
                        true
                    }
                    R.id.hybrid_map -> {
                        map.mapType = GoogleMap.MAP_TYPE_HYBRID
                        true
                    }
                    R.id.satellite_map -> {
                        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        true
                    }
                    R.id.terrain_map -> {
                        map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val currentLocation = lista[lista.size-1]
        val myHome = LatLng(currentLocation.latitude!!, currentLocation.longitude!!)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myHome, 25f))

        map = googleMap.apply {
            for (loc in lista) {
                val place = LatLng(loc.latitude!!, loc.longitude!!)
                marker = addMarker(MarkerOptions()
                    .position(place)
                    .title(loc.time)
                    .alpha(0.4f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                )
                marker?.showInfoWindow()
            }
        }

        setMapStyle(map)
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style_1
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }


}