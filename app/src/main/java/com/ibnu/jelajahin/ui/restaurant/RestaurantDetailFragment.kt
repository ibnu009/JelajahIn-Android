package com.ibnu.jelajahin.ui.restaurant

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.toJelajahinAccreditation
import com.ibnu.jelajahin.core.ui.adapter.ReviewWisataAdapter
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL
import com.ibnu.jelajahin.databinding.FragmentRestaurantDetailBinding
import com.ibnu.jelajahin.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels()

    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var restaurant: Restaurant

    private lateinit var reviewAdapter: ReviewWisataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateAppbar()
        val safeArgs = arguments?.let { RestaurantDetailFragmentArgs.fromBundle(it) }
        val uuid = safeArgs?.uuidRestaurant ?: ""

        initiateRecyclerViews()
        initiateUlasanData(uuid)

        viewModel.getRestaurantDetail(uuid).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showLoading(true)
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                    showLoading(false)
                }
                is ApiResponse.Success -> {
                    loadUiDetailRestaurant(response.data)
                    restaurant = response.data
                    showLoading(false)
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
    }

    private fun initiateRecyclerViews() {
        reviewAdapter = ReviewWisataAdapter()
        binding.rvUlasan.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUlasan.adapter = reviewAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun loadUiDetailRestaurant(restaurant: Restaurant) {
        binding.tvRestaurantName.text = restaurant.name
        binding.tvRestaurantLocation.text = restaurant.address
        binding.tvRestaurantDescription.text = restaurant.description
        binding.tvBusinessTime.text = "${restaurant.OpenTime} - ${restaurant.CloseTime}"
        binding.tvDiet.text = restaurant.foodType
        binding.tvPriceRange.text = "Rp ${restaurant.priceMin} - Rp ${restaurant.priceMax}"
        binding.restaurantStar.rating = restaurant.ratingAverage?.toFloat() ?: 0f
        binding.restaurantServiceStar.rating = restaurant.ratingService?.toFloat() ?: 0f
        binding.restaurantFoodStar.rating = restaurant.ratingFood?.toFloat() ?: 0f
        binding.restaurantCleanStar.rating = restaurant.ratingClean?.toFloat() ?: 0f

        binding.tvRestaurantAccreditation.text =
            if (restaurant.ratingAverage != null) restaurant.ratingAverage?.toJelajahinAccreditation() else (0.0).toJelajahinAccreditation()

        binding.tvRestaurantRating.text =
            if (restaurant.ratingAverage == null) "0.0" else restaurant.ratingAverage.toString()

        view?.let {
            Glide.with(it)
                .load(BASE_URL+restaurant.imageUrl)
                .into(binding.imgRestaurant)
        }

        binding.btnTambahUlasan.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToAddUlasan()
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

//        binding.btnBookmark.setOnClickListener {
//            it.popTap()
//            Handler(Looper.getMainLooper()).postDelayed({
//                if (isFavorite){
//                    isFavorite = !isFavorite
//                    binding.imgBookmark.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark))
//                } else {
//                    isFavorite = !isFavorite
//                    binding.imgBookmark.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmarked))
//                }
//            }, FAST_ANIMATION_TIME)
//        }
    }

    private fun initiateUlasanData(uuidWisata: String) {
        viewModel.getListReview(uuidWisata).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                }
                is ApiResponse.Success -> {
                    binding.tvEmptyUlasan.visibility = View.GONE
                    showLoading(false)
                    reviewAdapter.setData(response.data)
                }
                is ApiResponse.Empty -> {
                    binding.tvEmptyUlasan.visibility = View.VISIBLE
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }

        })
    }

    private fun navigateToAddUlasan() {
        val action =
            RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToUlasanRestaurantFragment(
                restaurant
            )
        findNavController().navigate(action)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun initiateAppbar() {
        binding.toolBar.setNavigationOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}