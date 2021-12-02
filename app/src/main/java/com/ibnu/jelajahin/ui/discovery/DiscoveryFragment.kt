package com.ibnu.jelajahin.ui.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.addMultipleMarkersForWisata
import com.ibnu.jelajahin.core.extention.boundsCameraToMarkers
import com.ibnu.jelajahin.core.extention.convertWisataToLatLng
import com.ibnu.jelajahin.databinding.DiscoveryFragmentBinding
import com.ibnu.jelajahin.ui.event.detail.EventDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
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

        val safeArgs = arguments?.let { DiscoveryFragmentArgs.fromBundle(it) }
        val provinceId = safeArgs?.provinceId ?: 0
        val cityId = safeArgs?.cityId ?: 0
        val type = safeArgs?.mapType ?: ""

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            viewModel.getWisataLocations(provinceId, cityId).observe(viewLifecycleOwner, {
                    response ->
                when(response){
                    is ApiResponse.Loading -> {
                        Timber.d("Loading")
                    }
                    is ApiResponse.Error -> {
                        Timber.d("Error ${response.errorMessage}")
                    }
                    is ApiResponse.Success -> {
                        googleMap.addMultipleMarkersForWisata(response.data)
                        val listLocations = response.data.convertWisataToLatLng()
                        googleMap.boundsCameraToMarkers(listLocations)
                        setButtonViews(googleMap, listLocations)
                    }
                    else -> {
                        Timber.d("Unknown Error")
                    }
                }
            })
        }
    }

    private fun setButtonViews(googleMap: GoogleMap, listLocation: List<LatLng>){
        binding.btnBounds.setOnClickListener {
            googleMap.boundsCameraToMarkers(listLocation)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}