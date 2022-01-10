package com.ibnu.jelajahin.ui.restaurant

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.local.entities.FavoriteEntity
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.showOKDialog
import com.ibnu.jelajahin.core.extention.toJelajahinAccreditation
import com.ibnu.jelajahin.core.ui.adapter.ReviewRestaurantAdapter
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentRestaurantDetailBinding
import com.ibnu.jelajahin.ui.wisata.WisataDetailFragmentDirections
import com.ibnu.jelajahin.utils.UiConstValue.FAST_ANIMATION_TIME
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.min

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels()

    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var restaurant: Restaurant

    private lateinit var reviewAdapter: ReviewRestaurantAdapter
    lateinit var pref: SharedPreferenceManager
    private var token: String = ""
    private var email: String = ""
    private var isFavorite = false


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

        pref = SharedPreferenceManager(requireContext())
        token = pref.getToken ?: ""
        email = pref.getEmail ?: ""

        Timber.d("Token is $token")

        initiateRecyclerViews()
        initiateDetailData(uuid)
        initiateUlasanData(uuid)

        binding.refresh.setOnRefreshListener {
            initiateDetailData(uuid)
        }
    }

    private fun initiateRecyclerViews() {
        reviewAdapter = ReviewRestaurantAdapter()
        binding.rvUlasan.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUlasan.adapter = reviewAdapter
    }

    private fun initiateDetailData(uuid: String) {
        viewModel.isAlreadyFavorite(uuid, email).observe(viewLifecycleOwner, {
            Timber.d("Favorite is $it")
            isFavorite = it
            getDetailData(uuid)
        })
    }

    private fun getDetailData(uuid: String) {
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

        if (restaurant.ratingAverage.toString().length > 3) {
            val maxLength: Int = min(restaurant.ratingAverage.toString().length, 3)
            binding.tvRestaurantRating.text =
                if (restaurant.ratingAverage == null) "0.0" else restaurant.ratingAverage.toString()
                    .substring(0, maxLength)
        } else {
            binding.tvRestaurantRating.text =
                if (restaurant.ratingAverage == null) "0.0" else restaurant.ratingAverage.toString()
        }

        binding.btnBookmark.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (!isFavorite) R.color.grey_600 else R.color.dusk_yellow
            )
        )

        view?.let {
            Glide.with(it)
                .load(BASE_URL + restaurant.imageUrl)
                .into(binding.imgRestaurant)
        }

        binding.btnTambahUlasan.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                if (token.isNotEmpty()) {
                    navigateToAddUlasan()
                } else {
                    requireContext().showOKDialog(
                        "Akses Ditolak!",
                        "Kamu harus memiliki akun JelajahIn jika ingin memberikan restaurant ini sebuah ulasan!"
                    )
                }
            }, FAST_ANIMATION_TIME)
        }

        initiateContactView(restaurant)

        binding.btnBookmark.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                if (token.isNotEmpty()) {
                    initiateBookmarkFunction()
                } else {
                    requireContext().showOKDialog(
                        "Akses Ditolak!",
                        "Kamu harus memiliki akun JelajahIn jika ingin memasukkan restaurant ini ke bookmark!"
                    )
                }
            }, FAST_ANIMATION_TIME)
        }
    }

    private fun initiateBookmarkFunction(){
        if (!isFavorite) {
            val favoriteEntity = FavoriteEntity(
                uuid = restaurant.uuidRestaurant,
                name = restaurant.name,
                address = restaurant.address,
                favoriteType = TypeUtils.FAVORITE_RESTAURANT,
                ratingAvg = restaurant.ratingAverage ?: 0.0,
                ratingCount = restaurant.ratingCount ?: 0,
                imageUrl = restaurant.imageUrl,
                savedBy = email
            )
            saveItemToFavorite(favoriteEntity)
        } else {
            removeItemFromFavorite(restaurant.uuidRestaurant, email)
        }
    }

    private fun saveItemToFavorite(favoriteEntity: FavoriteEntity) {
        viewModel.insertRestaurantToFavorite(favoriteEntity)
        isFavorite = !isFavorite
        binding.btnBookmark.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.dusk_yellow
            )
        )
        Timber.d("Saved ${favoriteEntity.name}")
    }

    private fun removeItemFromFavorite(uuidRestaurant: String, email: String) {
        isFavorite = !isFavorite
        binding.btnBookmark.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey_600
            )
        )
        viewModel.removeRestaurantFromFavorite(uuidRestaurant, email)
        Timber.d("Removed $uuidRestaurant with $email")
    }


    private fun initiateUlasanData(uuidRestaurant: String) {
        viewModel.getListReview(uuidRestaurant).observe(viewLifecycleOwner, { response ->
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
        viewModel.checkUserAlreadyReview(token, restaurant.uuidRestaurant).observe(viewLifecycleOwner, { isAlreadyReview ->
            if (!isAlreadyReview){
                val action =
                    RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToUlasanRestaurantFragment(
                        restaurant
                    )
                findNavController().navigate(action)
            } else{
                requireContext().showOKDialog(
                    "Akses Ditolak!",
                    "Kamu udah pernah memberikan ulasan kepada restaurant ini!"
                )
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.layoutLoading.visibility = View.VISIBLE
            binding.shimmeringDetail.startShimmer()
            binding.shimmeringDetail.showShimmer(true)
        } else {
            binding.shimmeringDetail.stopShimmer()
            binding.shimmeringDetail.showShimmer(false)
            binding.layoutLoading.visibility = View.GONE
        }
        binding.refresh.isRefreshing = isLoading
    }

    private fun initiateContactView(restaurant: Restaurant){
        when {
            restaurant.website.isNullOrEmpty() && restaurant.phone.isNullOrEmpty() -> {
                binding.contactComponent.root.visibility = View.GONE
            }
            restaurant.website?.isEmpty()!! -> {
                binding.contactComponent.layoutWeb.visibility = View.GONE
                binding.contactComponent.layoutEmail.visibility = View.GONE
            }
            restaurant.phone?.isEmpty()!! -> {
                binding.contactComponent.layoutTelfon.visibility = View.GONE
                binding.contactComponent.layoutEmail.visibility = View.GONE
            }
        }

        binding.contactComponent.layoutWeb.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                val action =
                    RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToWebViewFragment(
                        restaurant.website ?: ""
                    )
                findNavController().navigate(action)
            }, FAST_ANIMATION_TIME)
        }

        binding.contactComponent.layoutTelfon.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                Timber.d("Phone is not empty")
            }, FAST_ANIMATION_TIME)
        }
    }

    private fun initiateAppbar() {
        binding.toolBar.setNavigationOnClickListener {
            it.popTap()
            findNavController().popBackStack()
        }
        binding.appBarCoor.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            binding.refresh.isEnabled = verticalOffset == 0
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}