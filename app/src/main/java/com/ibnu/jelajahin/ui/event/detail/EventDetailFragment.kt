package com.ibnu.jelajahin.ui.event.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.*
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.EVENT_MARKER
import com.ibnu.jelajahin.databinding.EventDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private val viewModel: EventDetailViewModel by viewModels()

    private var _binding: EventDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventDetailFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.tvToolbarTitle.text = "Detail Event"
        binding.appBar.root.setBackgroundColor(Color.parseColor("#ffffff"))
        binding.appBar.imgBack.setOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }

        val safeArgs = arguments?.let { EventDetailFragmentArgs.fromBundle(it) }
        val eventUuid = safeArgs?.uuidEvent ?: ""

        setMapTouchHelper()
        viewModel.getEventDetail(eventUuid).observe(viewLifecycleOwner, { response ->
            when(response){
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showLoading(true)
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                    showLoading(false)
                }
                is ApiResponse.Success -> {
                    loadUiDetailEvent(response.data)
                    showLoading(false)
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
    }

    private fun loadUiDetailEvent(event: Event) {
        binding.tvEventName.text = event.name
        binding.tvEventDescription.text = event.description
        binding.tvEvenTicketPrice.text = event.ticketPrice
        binding.tvEventTime.text = event.schedule.parseHour()
        binding.tvEventDate.text = event.schedule.parseDateMonthAndYear()
        binding.cvReward.tvRewardPoint.text = context?.resources?.getString(
            R.string.points,
            event.pointReward.toString()
        )
        binding.cvReward.tvRewardXp.text =
            context?.resources?.getString(R.string.xp, event.xpReward.toString())

        view?.let {
            Glide.with(it)
                .load(event.imageURL)
                .into(binding.imgEvent)
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
            // Add a marker to event location and move the camera
            val eventLocation = LatLng(event.latitude, event.longtitude)
            mMap.addSingleMarker(eventLocation, event.name, EVENT_MARKER, event.uuidEvent)

            val zoomLevel = 18.0f
            val cu = CameraUpdateFactory.newLatLngZoom(eventLocation, zoomLevel)
            mMap.animateCamera(cu, 1000, null)
        }

        binding.btnHadiriEvent.setOnClickListener {
            it.popTap()
            compareCurrentTimeWithEventTime(event)
        }
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.shimmeringEventDetail.startShimmer()
            binding.shimmeringEventDetail.showShimmer(true)
            binding.shimmeringEventDetail.visibility = View.VISIBLE
            binding.detailEventContainer.visibility = View.GONE
        } else {
            binding.shimmeringEventDetail.stopShimmer()
            binding.shimmeringEventDetail.showShimmer(false)
            binding.shimmeringEventDetail.visibility = View.GONE
            binding.detailEventContainer.visibility = View.VISIBLE
        }
    }

    private fun compareCurrentTimeWithEventTime(event: Event) {

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
        val eventDate = parser.parse(event.schedule) ?: Date()

        val milliSeconds = eventDate.time - Calendar.getInstance().timeInMillis
        val days = TimeUnit.DAYS.convert(milliSeconds, TimeUnit.MILLISECONDS)
        val hours = TimeUnit.HOURS.convert(milliSeconds, TimeUnit.MILLISECONDS)
        val minutes = TimeUnit.MINUTES.convert(milliSeconds, TimeUnit.MILLISECONDS)
        when {
            days > 1 -> {
                requireContext().showOKDialog("Event Belum mulai", "Event akan dimulai $days Hari lagi")
            }
            hours > 0 -> {
                requireContext().showOKDialog("Event Belum mulai", "Event akan dimulai $hours Jam lagi")
            }
            minutes > 0 -> {
                requireContext().showOKDialog(
                    "Event Belum mulai",
                    "Event akan dimulai $minutes menit lagi"
                )
            }
            hours > -3 -> {
                val action = EventDetailFragmentDirections.actionEventDetailFragmentToAttendEventFragment(event)
                findNavController().navigate(action)
            }
            else -> {
                requireContext().showOKDialog(
                    "Event telah berakhir",
                    "Yahh.. Event ini telah selesai, yuk! ikutin event lainnya!"
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setMapTouchHelper() {
        binding.touchMap.setOnTouchListener { _, p1 ->
            when (p1?.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disallow ScrollView to intercept touch events.
                    binding.scrollEvent.requestDisallowInterceptTouchEvent(true)
                    // Disable touch on transparent view
                    false
                }
                MotionEvent.ACTION_UP -> {
                    // Allow ScrollView to intercept touch events.
                    binding.scrollEvent.requestDisallowInterceptTouchEvent(false)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    binding.scrollEvent.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}