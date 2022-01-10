package com.ibnu.jelajahin.ui.event.attend

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.request.PointBody
import com.ibnu.jelajahin.core.extention.parseToString
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.showOKDialog
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.LOCATION_PERMISSION
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentAttendEventBinding
import com.ibnu.jelajahin.ui.event.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AttendEventFragment : Fragment(), LocationListener {

    private val viewModel: EventViewModel by viewModels();

    private var _binding: FragmentAttendEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var myLocation: Location? = null
    private var event: Event? = null
    private var token: String = ""
    lateinit var pref: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendEventBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permReqLauncher.launch(LOCATION_PERMISSION)
        pref = SharedPreferenceManager(requireContext())
        token = pref.getToken ?: ""

        val safeArgs = arguments?.let { AttendEventFragmentArgs.fromBundle(it) }
        event = safeArgs?.event

        initiateAppbar()
        binding.tvEventName.text = event?.name
        binding.btnConfirm.setOnClickListener {
            it.popTap()
            compareLocations()
        }
    }

    private fun compareLocations() {
        val eventLocation = Location("locationA")
        eventLocation.latitude = event?.latitude ?: 0.0
        eventLocation.longitude = event?.longtitude ?: 0.0

        var distance = 35
        if (myLocation != null){
            distance = eventLocation.distanceTo(myLocation).toInt()
        }

        Timber.d("Location distance is $distance meter")
        if (distance > 20) {
            requireContext().showOKDialog(
                "Tidak di lokasi",
                "Oops sepertinya kamu masih terlalu jauh dari lokasi event nih"
            )
        } else {
            Timber.d("Posisi diterima")
            val currentTime = Calendar.getInstance().time.parseToString()

            val pointBody = PointBody(
                xpQuantity = event?.xpReward ?: 0,
                createdDate = currentTime,
                pointQuantity = event?.pointReward ?: 0,
                transactionType = 1,
                uuidEvent = event?.uuidEvent ?: ""
            )
            viewModel.addUserPoint(token, pointBody).observe(viewLifecycleOwner, {
                    response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        Timber.d("Loading")
                        showLoading(true)
                    }
                    is ApiResponse.Error -> {
                        showLoading(false)
                        Timber.d("Error ${response.errorMessage}")
                    }
                    is ApiResponse.Success -> {
                        showLoading(false)
                        Timber.d("Success ${response.data}")
                        val action = event?.let { AttendEventFragmentDirections.actionAttendEventFragmentToSuccessAttendFragment(it) }
                        action?.let { findNavController().navigate(it) }
                    }
                    else -> {
                        Timber.d("Unknown Error")
                    }
                }
            })
        }
    }

    private fun initiateAppbar() {
        binding.appBar.tvToolbarTitle.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.appBar.tvToolbarTitle.text = "Konfirmasi Kehadiran"
        binding.appBar.imgBack.setColorFilter(Color.argb(255, 255, 255, 255)) //white tint
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
                    permReqLauncher.launch(LOCATION_PERMISSION)
                    p0.dismiss()
                }
                .create()
                .show()
        }
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
            val eventLocation = LatLng(event?.latitude ?: 0.0, event?.longtitude ?: 0.0)
            mMap.addMarker(MarkerOptions().position(eventLocation).title(event?.name))
            mMap.isMyLocationEnabled = true
            val zoomLevel = 18.0f
            val cu = CameraUpdateFactory.newLatLngZoom(eventLocation, zoomLevel)
            mMap.animateCamera(cu, 1000, null)
        }

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 10000, 5f,
            this
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnConfirm.isClickable = !isLoading
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onLocationChanged(p0: Location) {
        Timber.d("location is ${p0.latitude} + ${p0.longitude}")
        myLocation = p0
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
    }
}