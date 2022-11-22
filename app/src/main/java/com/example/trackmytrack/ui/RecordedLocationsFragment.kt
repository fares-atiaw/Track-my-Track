package com.example.trackmytrack.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trackmytrack.MyApp
import com.example.trackmytrack.R
import com.example.trackmytrack.adapter.RecordsAdapter
import com.example.trackmytrack.databinding.FragmentRecordedLocationsBinding
import com.example.trackmytrack.utils.KEY_METERS_RECORDED
import com.example.trackmytrack.utils.Response

class RecordedLocationsFragment : Fragment(R.layout.fragment_recorded_locations) {
    private lateinit var binding : FragmentRecordedLocationsBinding
    private val adapter = RecordsAdapter()
    val viewModel by activityViewModels<MainViewModel> {
        MainViewModel.MainViewModelFactory(
            (requireContext().applicationContext as MyApp).repository
        )
    }
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var meters = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordedLocationsBinding.inflate(inflater)
        setupRecyclerView()
        binding.lifecycleOwner = this
        sharedPreference = requireContext().getSharedPreferences("PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPreference.edit()

        viewModel.allTrackRecordsList.data?.observe(viewLifecycleOwner) { result ->
            Log.e("2nd Fragment", result.toString())
            adapter.submitList(result)
            meters = result.size * 5
            binding.tvMeters.text = "Track= $meters meters"
            binding.rvLocations.smoothScrollToPosition(result.size);
        }

        binding.fabMap.setOnClickListener{
            findNavController().navigate(R.id.action_recordedLocationsFragment_to_mapFragment)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvLocations.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        editor.putInt(KEY_METERS_RECORDED, meters)
        binding.unbind()
    }

}
