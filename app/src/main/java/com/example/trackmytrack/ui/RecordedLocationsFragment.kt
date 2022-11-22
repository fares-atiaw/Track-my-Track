package com.example.trackmytrack.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trackmytrack.MyApp
import com.example.trackmytrack.R
import com.example.trackmytrack.adapter.RecordsAdapter
import com.example.trackmytrack.databinding.FragmentRecordedLocationsBinding
import com.example.trackmytrack.utils.Response

class RecordedLocationsFragment : Fragment(R.layout.fragment_recorded_locations) {
    private lateinit var binding : FragmentRecordedLocationsBinding
    private val adapter = RecordsAdapter()
    val viewModel by activityViewModels<MainViewModel> {
        MainViewModel.MainViewModelFactory(
            (requireContext().applicationContext as MyApp).repository
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordedLocationsBinding.inflate(inflater)
        setupRecyclerView()

        binding.lifecycleOwner = this

        viewModel.allTrackRecordsList.data?.observe(viewLifecycleOwner) { result ->
            Log.e("2nd Fragment", result.toString())
            adapter.submitList(result)
        }


        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvLocations.adapter = adapter
        binding.rvLocations.setHasFixedSize(true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

}
