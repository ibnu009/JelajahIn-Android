package com.ibnu.jelajahin.ui.wisata

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
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.showOKDialog
import com.ibnu.jelajahin.core.extention.toJelajahinAccreditation
import com.ibnu.jelajahin.core.ui.adapter.ReviewWisataAdapter
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL_IMAGE
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentWisataDetailBinding
import com.ibnu.jelajahin.utils.UiConstValue.FAST_ANIMATION_TIME
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.min

@AndroidEntryPoint
class WisataDetailFragment : Fragment() {

    private val viewModel: WisataViewModel by viewModels()

    private var _binding: FragmentWisataDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var wisata: Wisata
    private var isFavorite = false

    private lateinit var reviewAdapter: ReviewWisataAdapter
    lateinit var pref: SharedPreferenceManager
    private var token: String = ""
    private var email: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWisataDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateAppbar()
        val safeArgs = arguments?.let { WisataDetailFragmentArgs.fromBundle(it) }
        val uuidWisata = safeArgs?.uuidWisata ?: ""

        pref = SharedPreferenceManager(requireContext())
        token = pref.getToken ?: ""
        email = pref.getEmail ?: ""

        Timber.d("Token is $token")
        Timber.d("Email is $email")


        initiateRecyclerViews()
        initiateDetailData(uuidWisata)
        initiateUlasanData(uuidWisata)

        binding.refresh.setOnRefreshListener {
            initiateDetailData(uuidWisata)
        }
    }

    private fun initiateDetailData(uuidWisata: String) {
        viewModel.isAlreadyFavorite(uuidWisata, email).observe(viewLifecycleOwner, {
            Timber.d("Favorite is $it")
            isFavorite = it
            getDetailData(uuidWisata)
        })
    }

    private fun getDetailData(uuidWisata: String) {
        viewModel.getWisataDetail(uuidWisata).observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    Timber.d("Loading")
                    showLoading(true)
                }
                is ApiResponse.Error -> {
                    Timber.d("Error ${response.errorMessage}")
                    showLoading(false)
                    binding.refresh.isRefreshing = false
                }
                is ApiResponse.Success -> {
                    loadUiDetailWisata(response.data)
                    wisata = response.data
                    showLoading(false)
                    binding.refresh.isRefreshing = false
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
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

    private fun initiateRecyclerViews() {
        reviewAdapter = ReviewWisataAdapter()
        binding.rvUlasan.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUlasan.adapter = reviewAdapter
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

    private fun loadUiDetailWisata(wisata: Wisata) {
        binding.tvWisataName.text = wisata.name
        binding.tvWisataLocation.text = wisata.address
        binding.tvWisataDescription.text = wisata.description
        binding.tvTicketPrice.text = wisata.ticketPrice
        binding.tvTicketPriceWeekend.text = wisata.ticketPrice
        binding.wisataStar.rating = wisata.ratingAverage.toFloat()
        binding.tvWisataAccreditation.text = wisata.ratingAverage.toJelajahinAccreditation()

        if (wisata.ratingAverage.toString().length > 3) {
            val maxLength: Int = min(wisata.ratingAverage.toString().length, 3)
            binding.tvWisataRating.text =
                if (wisata.ratingAverage == 0.0) "0.0" else wisata.ratingAverage.toString()
                    .substring(0, maxLength)
        } else {
            binding.tvWisataRating.text =
                if (wisata.ratingAverage == 0.0) "0.0" else wisata.ratingAverage.toString()
        }


        view?.let {
            Glide.with(it)
                .load(BASE_URL_IMAGE + wisata.imageUrl)
                .into(binding.imgWisata)
        }

        binding.btnBookmark.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (!isFavorite) R.color.grey_600 else R.color.dusk_yellow
            )
        )

        binding.btnTambahUlasan.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                if (token.isNotEmpty()) {
                    navigateToAddUlasan()
                } else {
                    requireContext().showOKDialog(
                        "Akses Ditolak!",
                        "Kamu harus memiliki akun JelajahIn jika ingin memberikan wisata ini sebuah ulasan!"
                    )
                }
            }, FAST_ANIMATION_TIME)
        }

        binding.btnBookmark.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                if (token.isNotEmpty()) {
                    initiateBookmarkFunction()
                } else {
                    requireContext().showOKDialog(
                        "Akses Ditolak!",
                        "Kamu harus memiliki akun JelajahIn jika ingin memasukkan wisata ini ke bookmark!"
                    )
                }
            }, FAST_ANIMATION_TIME)
        }
    }

    private fun initiateBookmarkFunction() {
        if (!isFavorite) {
            val favoriteEntity = FavoriteEntity(
                uuid = wisata.uuidWisata,
                name = wisata.name,
                address = wisata.address,
                favoriteType = TypeUtils.FAVORITE_WISATA,
                ratingAvg = wisata.ratingAverage,
                ratingCount = wisata.ratingCount,
                imageUrl = wisata.imageUrl,
                savedBy = email
            )
            saveItemToFavorite(favoriteEntity)
        } else {
            removeItemFromFavorite(wisata.uuidWisata, email)
        }
    }

    private fun saveItemToFavorite(favoriteEntity: FavoriteEntity) {
        viewModel.insertWisataToFavorite(favoriteEntity)
        isFavorite = !isFavorite
        binding.btnBookmark.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.dusk_yellow
            )
        )
        Timber.d("Saved ${favoriteEntity.name}")
    }

    private fun removeItemFromFavorite(uuidWisata: String, email: String) {
        isFavorite = !isFavorite
        binding.btnBookmark.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey_600
            )
        )
        viewModel.removeWisataFromFavorite(uuidWisata, email)
        Timber.d("Removed $uuidWisata with $email")
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
    }

    private fun navigateToAddUlasan() {
        viewModel.checkUserAlreadyReview(token, wisata.uuidWisata)
            .observe(viewLifecycleOwner, { isAlreadyReview ->
                if (!isAlreadyReview) {
                    val action =
                        WisataDetailFragmentDirections.actionWisataDetailFragmentToUlasanWisataFragment(
                            wisata
                        )
                    findNavController().navigate(action)
                } else {
                    requireContext().showOKDialog(
                        "Akses Ditolak!",
                        "Kamu udah pernah memberikan ulasan kepada wisata ini!"
                    )
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}