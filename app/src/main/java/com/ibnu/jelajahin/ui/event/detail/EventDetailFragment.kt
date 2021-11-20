package com.ibnu.jelajahin.ui.event.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.databinding.EventDetailFragmentBinding
import com.ibnu.jelajahin.core.extention.parseDateMonthAndYear
import com.ibnu.jelajahin.core.extention.parseHour
import com.ibnu.jelajahin.core.extention.popTap
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import android.os.CountDownTimer
import android.util.Log
import com.ibnu.jelajahin.core.extention.showOKDialog
import okhttp3.internal.format
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private val viewModel: EventDetailViewModel by viewModels()

    private var _binding: EventDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private var timer: CountDownTimer? = null

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
        binding.appBar.imgBack.setOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }

        val safeArgs = arguments?.let { EventDetailFragmentArgs.fromBundle(it) }
        val eventUuid = safeArgs?.uuidEvent ?: ""

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

    private fun loadUiDetailEvent(event: Event){
        binding.tvEventName.text = event.name
        binding.tvEventDescription.text = event.description
        binding.tvEvenTicketPrice.text = event.ticketPrice
        binding.tvEventTime.text = event.schedule.parseHour()
        binding.tvEventDate.text = event.schedule.parseDateMonthAndYear()
        binding.cvReward.tvRewardPoint.text = context?.resources?.getString(R.string.points, event.pointReward.toString())
        binding.cvReward.tvRewardXp.text = context?.resources?.getString(R.string.xp, event.xpReward.toString())

        view?.let {
            Glide.with(it)
                .load(event.imageURL)
                .into(binding.imgEvent)
        }

        binding.btnHadiriEvent.setOnClickListener {
            it.popTap()
            compareCurrentTimeWithEventTime(event.schedule)
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

    private fun compareCurrentTimeWithEventTime(eventSchedule: String) {

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)
        val eventDate = parser.parse(eventSchedule) ?: Date()

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
                requireContext().showOKDialog("Event Belum mulai", "Event akan dimulai $minutes menit lagi")
            }
            hours > -3 -> {
                Timber.d("Udah mulai!")
            }
            else -> {
                requireContext().showOKDialog("Event telah berakhir", "Yahh.. Event ini telah selesai, yuk! ikutin event lainnya!")
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}