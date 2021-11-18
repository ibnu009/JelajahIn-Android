package com.ibnu.jelajahin.ui.event.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.databinding.EventDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private val viewModel: EventDetailViewModel by viewModels()

    private var _binding: EventDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventDetailFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.tvEventTime.text = "09:00 WIB"
        binding.tvEventDate.text = event.schedule
        binding.cvReward.tvRewardPoint.text = context?.resources?.getString(R.string.points, event.pointReward.toString())
        binding.cvReward.tvRewardXp.text = context?.resources?.getString(R.string.xp, event.xpReward.toString())

        view?.let {
            Glide.with(it)
                .load(event.imageURL)
                .into(binding.imgEvent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}