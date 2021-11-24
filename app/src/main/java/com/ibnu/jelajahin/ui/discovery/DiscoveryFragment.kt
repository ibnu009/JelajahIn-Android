package com.ibnu.jelajahin.ui.discovery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.extention.addSingleMarker
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.databinding.DiscoveryFragmentBinding

class DiscoveryFragment : Fragment() {

    private val viewModel: DiscoveryViewModel by viewModels();

    private var _binding: DiscoveryFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DiscoveryFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}