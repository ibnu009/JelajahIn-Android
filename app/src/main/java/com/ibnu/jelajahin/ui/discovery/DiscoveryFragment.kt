package com.ibnu.jelajahin.ui.discovery

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.GeoApiContext
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.map.addMultipleMarkersForGem
import com.ibnu.jelajahin.core.extention.map.boundsCameraToMarkers
import com.ibnu.jelajahin.core.extention.map.MapDirectionHelper
import com.ibnu.jelajahin.core.extention.map.convertGemToLatLng
import com.ibnu.jelajahin.core.extention.showOKDialog
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.databinding.DiscoveryFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DiscoveryFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private val viewModel: DiscoveryViewModel by viewModels()

    private var _binding: DiscoveryFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var myLocation: Location? = null
    private var selectedLocation: LatLng? = null
    private lateinit var geoContext: GeoApiContext

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

        geoContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .build()

        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
            permReqLauncher.launch(JelajahinConstValues.LOCATION_PERMISSION)
            viewModel.getAllGems(15, 0)
                .observe(viewLifecycleOwner, { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            Timber.d("Loading")
                        }
                        is ApiResponse.Error -> {
                            Timber.d("Error ${response.errorMessage}")
                        }
                        is ApiResponse.Success -> {
                            googleMap.addMultipleMarkersForGem(response.data)
                            val listLocations = response.data.convertGemToLatLng()
                            googleMap.boundsCameraToMarkers(listLocations)
                            setBoundBetweenLocations(googleMap, listLocations)
                            getPathFromMyLocationToTargetLocation()

                        }
                        else -> {
                            Timber.d("Unknown Error")
                        }
                    }
                })
        }
    }

    private fun setBoundBetweenLocations(googleMap: GoogleMap, listLocation: List<LatLng>) {
        binding.btnBounds.setOnClickListener {
            googleMap.boundsCameraToMarkers(listLocation)
        }
    }

    private fun getPathFromMyLocationToTargetLocation() {
        binding.btnShowPath.setOnClickListener {
            requireContext().showOKDialog("Upcoming Feature!","Fitur ini bakalan ada di versi selanjutnya! <3")
            if (selectedLocation == null) {
                Toast.makeText(requireContext(), "Belum memilih lokasi", Toast.LENGTH_SHORT).show()
            } else {
                myLocation?.let { loc ->
                    MapDirectionHelper().getPath(
                        geoContext,
                        loc,
                        targetLocation = selectedLocation!!
                    )
                }
            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AlertDialog.Builder(requireContext())
                .setTitle("Location Permission Needed")
                .setMessage("This app needs the Location permission, please accept to use location functionality")
                .setPositiveButton(
                    "OK"
                ) { p0, _ ->
                    permReqLauncher.launch(JelajahinConstValues.LOCATION_PERMISSION)
                    p0.dismiss()
                }
                .create()
                .show()
        }

        mMap.isMyLocationEnabled = true
        mMap.setOnMarkerClickListener(this)

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        val locationListener = LocationListener { p0 ->
            Timber.d("location is ${p0.latitude} + ${p0.longitude}")
            myLocation = p0
        }
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0, 0f,
            locationListener
        )
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                getLocation()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        selectedLocation = p0.position
        Timber.d("selected latitude is ${p0.position.latitude}")
        return false
    }

}