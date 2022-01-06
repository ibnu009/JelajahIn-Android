package com.ibnu.jelajahin.ui.event.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.*
import com.ibnu.jelajahin.core.extention.map.addSingleMarker
import com.ibnu.jelajahin.core.extention.map.animateCameraToSingleMarker
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.EVENT_MARKER
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.EventDetailFragmentBinding
import com.ibnu.jelajahin.utils.UiConstValue
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

    lateinit var pref: SharedPreferenceManager
    private var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventDetailFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SharedPreferenceManager(requireContext())
        token = pref.getToken ?: ""
        Timber.d("Token is $token")

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

        setScheduleText(event)

        binding.cvReward.tvRewardPoint.text = context?.resources?.getString(
            R.string.points,
            event.pointReward.toString()
        )
        binding.cvReward.tvRewardXp.text =
            context?.resources?.getString(R.string.xp, event.xpReward.toString())

        view?.let {
            Glide.with(it)
                .load(BASE_URL + event.imageURL)
                .into(binding.imgEvent)
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.containerMap) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
            // Add a marker to event location and move the camera
            val eventLocation = LatLng(event.latitude, event.longtitude)
            mMap.addSingleMarker(eventLocation, event.name, EVENT_MARKER, event.uuidEvent, requireContext())
            mMap.animateCameraToSingleMarker(eventLocation)
        }

        binding.btnHadiriEvent.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                if (token.isNotEmpty()) {
                    viewModel.checkUserIsAlreadyAttendEvent(event.uuidEvent)
                        .observe(viewLifecycleOwner, { isAlreadyAttend ->
                            if (isAlreadyAttend) {
                                requireContext().showOKDialog(
                                    "Sudah Menghadiri!",
                                    "Kamu sudah menghadiri event ini dan mendapatkan point, kamu tidak bisa lagi menghadiri event ini lagi!"
                                )
                            } else {
                                compareCurrentTimeWithEventTime(event)
                            }
                        })
                } else {
                    requireContext().showOKDialog("Akses Ditolak!", "Kamu harus memiliki akun JelajahIn jika ingin menghadiri event ini!")
                }
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setScheduleText(event: Event){
        val eventStart = event.startDate.parseDateMonthAndYear()
        val eventEnd = event.endDate.parseDateMonthAndYear()

        val isSameMonth = event.startDate.isThisDateIsTheSameMonthAs(event.endDate)
        val isSameDay = event.startDate.isThisDateIsTheSameDayAs(event.endDate)
        val year = event.startDate.parseDateToYearOnly()

        if (isSameMonth){
            val month = event.startDate.parseDateToMonthOnly()
            val dateStart = event.startDate.parseDateToDateOnly()
            val dateEnd = event.endDate.parseDateToDateOnly()
            val hourStart = event.startDate.parseHour()
            val hourEnd = event.endDate.parseHour()
            if (isSameDay){
                binding.tvEventDate.text = "$dateStart $month $year"
                binding.tvEventTime.text = "$hourStart-$hourEnd"
            } else {
                binding.tvEventDate.text = "$dateStart-$dateEnd $month $year"
                binding.tvEventTime.text = hourStart
            }
        } else {
            binding.tvEventDate.text = "$eventStart-$eventEnd"
            binding.tvEventTime.text = event.startDate.parseHour()
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
        val eventStartDate = parser.parse(event.startDate) ?: Date()
        val eventEndDate = parser.parse(event.endDate) ?: Date()
        val currentTime = Calendar.getInstance().time.time

        val milliSeconds = eventStartDate.time - currentTime
        val days = TimeUnit.DAYS.convert(milliSeconds, TimeUnit.MILLISECONDS)
        val hours = TimeUnit.HOURS.convert(milliSeconds, TimeUnit.MILLISECONDS)
        val minutes = TimeUnit.MINUTES.convert(milliSeconds, TimeUnit.MILLISECONDS)
        when {
            days > 1 -> {
                requireContext().showOKDialog("Event Belum mulai", "Event akan dimulai $days Hari lagi")
            }
            hours > 0 -> {
                val remainingMinutes = minutes - (hours * 60)
                requireContext().showOKDialog("Event Belum mulai", "Event akan dimulai $hours Jam dan $remainingMinutes menit lagi")
            }
            minutes > 0 -> {
                requireContext().showOKDialog(
                    "Event Belum mulai",
                    "Event akan dimulai $minutes menit lagi"
                )
            }
            eventEndDate.time > currentTime -> {
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