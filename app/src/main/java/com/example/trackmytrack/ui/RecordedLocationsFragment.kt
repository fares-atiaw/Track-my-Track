package com.example.trackmytrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackmytrack.R
import com.example.trackmytrack.adapter.RecordsAdapter
import com.example.trackmytrack.databinding.FragmentRecordedLocationsBinding

class RecordedLocationsFragment : Fragment(R.layout.fragment_recorded_locations) {
    lateinit var binding : FragmentRecordedLocationsBinding
    private val adapter = RecordsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordedLocationsBinding.inflate(inflater)
        setupRecyclerView()


        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvLocations.adapter = adapter
        binding.rvLocations.setHasFixedSize(true)
    }

}
