package com.ibnu.jelajahin.ui.penginapan

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
import com.ibnu.jelajahin.core.extention.map.addMultipleMarkersForPenginapan
import com.ibnu.jelajahin.core.extention.map.boundsCameraToMarkers
import com.ibnu.jelajahin.core.extention.map.convertPenginapanToLatLng
import com.ibnu.jelajahin.core.ui.gwindow.InfoWindowsRestaurant
import com.ibnu.jelajahin.databinding.FragmentPenginapanMapBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PenginapanMapFragment : Fragment() {

    private val viewModel: PenginapanViewModel by viewModels()

    private var _binding: FragmentPenginapanMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPenginapanMapBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            viewModel.getPenginapanLocations(0)
                .observe(viewLifecycleOwner, { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            Timber.d("Loading")
                        }
                        is ApiResponse.Error -> {
                            Timber.d("Error ${response.errorMessage}")
                        }
                        is ApiResponse.Success -> {
                            googleMap.addMultipleMarkersForPenginapan(
                                response.data,
                                requireContext()
                            )
                            val listLocations = response.data.convertPenginapanToLatLng()
                            googleMap.boundsCameraToMarkers(listLocations)
                            setButtonViews(googleMap, listLocations)

                            val infoWindow =
                                InfoWindowsRestaurant(requireActivity(), requireContext())
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