package com.ibnu.jelajahin.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.ui.adapter.AdsAdapter
import com.ibnu.jelajahin.core.ui.adapter.handler.AdsItemHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.FRAGMENT_DETAIL_EVENT
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.FRAGMENT_DETAIL_HOTEL
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.FRAGMENT_DETAIL_RESTAURANT
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.FRAGMENT_DETAIL_WISTA
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.OPEN_FRAGMENT
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.OPEN_WEBVIEW
import com.ibnu.jelajahin.databinding.FragmentHomeBinding
import com.ibnu.jelajahin.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(), AdsItemHandler {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adsAdapter: AdsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLihatWisata.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_homeFragment_to_wisataFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnLihatRestaurant.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_homeFragment_to_penginapanFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnLihatRestaurant.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_homeFragment_to_restaurantFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        initiateRecyclerViews()
        initiateAds()
    }

    private fun initiateRecyclerViews() {
        adsAdapter = AdsAdapter(this)
        binding.rvAdvertisement.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvAdvertisement.adapter = adsAdapter
    }

    private fun initiateAds() {
        viewModel.getAds(15).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                }
                is ApiResponse.Success -> {
                    adsAdapter.setData(response.data)
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBannerClicked(
        actionType: String,
        actionValue: String?,
        actionParam: String?
    ) {
        when (actionType) {
            OPEN_WEBVIEW -> {
                Timber.d("Open webview")
            }
            OPEN_FRAGMENT -> {
                actionValue?.let { actionVal -> openFragment(actionVal, actionParam) }
            }
        }
    }

    private fun openFragment(actionValue: String, actionParam: String?) {
        when (actionValue) {
            FRAGMENT_DETAIL_EVENT -> {
                Timber.d("Open fragment event")
                val action = HomeFragmentDirections.actionHomeFragmentToEventDetailFragment(
                    actionParam ?: ""
                )
                findNavController().navigate(action)
            }
            FRAGMENT_DETAIL_HOTEL -> {
                Timber.d("Open fragment hotel")
            }
            FRAGMENT_DETAIL_RESTAURANT -> {
                Timber.d("Open fragment restaurant")
            }
            FRAGMENT_DETAIL_WISTA -> {
                Timber.d("Open fragment wisata")
                val action = HomeFragmentDirections.actionHomeFragmentToWisataDetailFragment(
                    actionParam ?: ""
                )
                findNavController().navigate(action)
            }
        }
    }

}