package com.ibnu.jelajahin.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.showExitJelajahInDialog
import com.ibnu.jelajahin.core.ui.adapter.AdsAdapter
import com.ibnu.jelajahin.core.ui.adapter.RecommendedPenginapanAdapter
import com.ibnu.jelajahin.core.ui.adapter.RecommendedRestaurantAdapter
import com.ibnu.jelajahin.core.ui.adapter.RecommendedWisataAdapter
import com.ibnu.jelajahin.core.ui.adapter.handler.AdsItemHandler
import com.ibnu.jelajahin.core.ui.adapter.handler.RecommendationItemClickHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.FRAGMENT_DETAIL_EVENT
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.FRAGMENT_DETAIL_HOTEL
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.FRAGMENT_DETAIL_RESTAURANT
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.FRAGMENT_DETAIL_WISTA
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.OPEN_FRAGMENT
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.OPEN_WEBVIEW
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.RECOMMENDATION_PENGINAPAN_TYPE
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.RECOMMENDATION_RESTAURANT_TYPE
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.RECOMMENDATION_WISATA_TYPE
import com.ibnu.jelajahin.databinding.FragmentHomeBinding
import com.ibnu.jelajahin.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(), AdsItemHandler, RecommendationItemClickHandler {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adsAdapter: AdsAdapter
    private lateinit var wisataRecommendationAdapter: RecommendedWisataAdapter
    private lateinit var penginapanRecommendationAdapter: RecommendedPenginapanAdapter
    private lateinit var restaurantRecommendationAdapter: RecommendedRestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().showExitJelajahInDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        showBannerLoading(true)

        binding.btnLihatWisata.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_homeFragment_to_wisataFragment)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.btnLihatPenginapan.setOnClickListener {
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
        initiateRecommendedWisata()
        initiateRecommendedRestaurant()
        initiateRecommendedPenginapan()

        binding.refreshLayout.setOnRefreshListener {
            initiateAds()
            initiateRecommendedWisata()
            initiateRecommendedRestaurant()
            initiateRecommendedPenginapan()
        }

    }

    private fun initiateRecyclerViews() {
        //banner adapter
        adsAdapter = AdsAdapter(this)
        binding.rvAdvertisement.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvAdvertisement.adapter = adsAdapter

        //wisata adapter
        wisataRecommendationAdapter = RecommendedWisataAdapter(this)
        binding.rvWisataRecommendation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvWisataRecommendation.adapter = wisataRecommendationAdapter

        //restaurant adapter
        restaurantRecommendationAdapter = RecommendedRestaurantAdapter(this)
        binding.rvRestaurantRecommendation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRestaurantRecommendation.adapter = restaurantRecommendationAdapter

        //penginapan adapter
        penginapanRecommendationAdapter = RecommendedPenginapanAdapter(this)
        binding.rvPenginapanRecommendation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPenginapanRecommendation.adapter = penginapanRecommendationAdapter
    }

    private fun initiateAds() {
        viewModel.getAds(15).observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
//                    showBannerLoading(true)
                }
                is ApiResponse.Error -> {
//                    showBannerLoading(false)
                    Timber.d("Error ${response.errorMessage}")
                }
                is ApiResponse.Success -> {
//                    showBannerLoading(false)
                    adsAdapter.setData(response.data)
                }
                else -> {
//                    showBannerLoading(false)
                    Timber.d("Unknown Error")
                }
            }
        }
    }

    private fun initiateRecommendedWisata() {
        viewModel.getWisataRecommendation().observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showWisataLoading(true)
                }
                is ApiResponse.Error -> {
                    showWisataLoading(false)
                    Timber.d("Error ${response.errorMessage}")
                    binding.refreshLayout.isRefreshing = false
                }
                is ApiResponse.Success -> {
                    showWisataLoading(false)
                    binding.refreshLayout.isRefreshing = false
                    wisataRecommendationAdapter.setData(response.data)
                }
                else -> {
                    showWisataLoading(false)
                    binding.refreshLayout.isRefreshing = false
                    Timber.d("Unknown Error")
                }
            }
        }
    }

    private fun initiateRecommendedRestaurant() {
        viewModel.getRestaurantRecommendation().observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showRestaurantLoading(true)
                }
                is ApiResponse.Error -> {
                    showRestaurantLoading(false)
                    Timber.d("Error ${response.errorMessage}")
                }
                is ApiResponse.Success -> {
                    showRestaurantLoading(false)
                    restaurantRecommendationAdapter.setData(response.data)
                }
                else -> {
                    showRestaurantLoading(false)
                    Timber.d("Unknown Error")
                }
            }
        }
    }

    private fun initiateRecommendedPenginapan() {
        viewModel.getPenginapanRecommendation().observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showPenginapanLoading(true)
                }
                is ApiResponse.Error -> {
                    showPenginapanLoading(false)
                    Timber.d("Error ${response.errorMessage}")
                }
                is ApiResponse.Success -> {
                    showPenginapanLoading(false)
                    penginapanRecommendationAdapter.setData(response.data)
                }
                else -> {
                    showPenginapanLoading(false)
                    Timber.d("Unknown Error")
                }
            }
        }
    }


    private fun showWisataLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.wisataShimmeringLoading.startShimmer()
            binding.wisataShimmeringLoading.showShimmer(true)
            binding.wisataShimmeringLoading.visibility = View.VISIBLE
        } else {
            binding.wisataShimmeringLoading.stopShimmer()
            binding.wisataShimmeringLoading.showShimmer(false)
            binding.wisataShimmeringLoading.visibility = View.INVISIBLE
        }
    }

    private fun showRestaurantLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.restaurantShimmeringLoading.startShimmer()
            binding.restaurantShimmeringLoading.showShimmer(true)
            binding.restaurantShimmeringLoading.visibility = View.VISIBLE
        } else {
            binding.restaurantShimmeringLoading.stopShimmer()
            binding.restaurantShimmeringLoading.showShimmer(false)
            binding.restaurantShimmeringLoading.visibility = View.INVISIBLE
        }
    }

    private fun showPenginapanLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.penginapanShimmeringLoading.startShimmer()
            binding.penginapanShimmeringLoading.showShimmer(true)
            binding.penginapanShimmeringLoading.visibility = View.VISIBLE
        } else {
            binding.penginapanShimmeringLoading.stopShimmer()
            binding.penginapanShimmeringLoading.showShimmer(false)
            binding.penginapanShimmeringLoading.visibility = View.INVISIBLE
        }
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
                val action =
                    HomeFragmentDirections.actionHomeFragmentToWebViewFragment(actionValue ?: "")
                findNavController().navigate(action)
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

    override fun onItemClicked(uuid: String, type: String) {
        when (type) {
            RECOMMENDATION_WISATA_TYPE -> {
                val action = HomeFragmentDirections.actionHomeFragmentToWisataDetailFragment(uuid)
                findNavController().navigate(action)
            }
            RECOMMENDATION_RESTAURANT_TYPE -> {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToRestaurantDetailFragment(uuid)
                findNavController().navigate(action)
            }
            RECOMMENDATION_PENGINAPAN_TYPE -> {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToPenginapanDetailFragment(uuid)
                findNavController().navigate(action)
            }
        }
    }

}