package com.ibnu.jelajahin.ui.restaurant

import android.annotation.SuppressLint
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
import com.ibnu.jelajahin.core.extention.map.*
import com.ibnu.jelajahin.core.utils.InfoWindowsRestaurant
import com.ibnu.jelajahin.databinding.FragmentRestaurantMapBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RestaurantMapFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels();

    private var _binding: FragmentRestaurantMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantMapBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            viewModel.getRestaurantLocations(0)
                .observe(viewLifecycleOwner, { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            Timber.d("Loading")
                        }
                        is ApiResponse.Error -> {
                            Timber.d("Error ${response.errorMessage}")
                        }
                        is ApiResponse.Success -> {
                            googleMap.addMultipleMarkersForRestaurant(response.data, requireContext())
                            val listLocations = response.data.convertRestaurantToLatLng()
                            googleMap.boundsCameraToMarkers(listLocations)
                            setButtonViews(googleMap, listLocations)

                            val infoWindow = InfoWindowsRestaurant(requireContext())
                            googleMap.setInfoWindowAdapter(infoWindow)
                        }
                        else -> {
                            Timber.d("Unknown Error")
                        }
                    }
                })
        }
    }

    private fun setButtonViews(googleMap: GoogleMap, listLocation: List<LatLng>) {
        binding.btnBounds.setOnClickListener {
            googleMap.boundsCameraToMarkers(listLocation)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}