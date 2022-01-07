package com.ibnu.jelajahin.ui.penginapan

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.local.entities.FavoriteEntity
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils
import com.ibnu.jelajahin.core.data.model.Penginapan
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.showOKDialog
import com.ibnu.jelajahin.core.extention.toJelajahinAccreditation
import com.ibnu.jelajahin.core.ui.adapter.HotelFacilityAdapter
import com.ibnu.jelajahin.core.ui.adapter.ReviewPenginapanAdapter
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import com.ibnu.jelajahin.databinding.FragmentPenginapanDetailBinding
import com.ibnu.jelajahin.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.min

@AndroidEntryPoint
class PenginapanDetailFragment : Fragment() {

    private val viewModel: PenginapanViewModel by viewModels()

    private var _binding: FragmentPenginapanDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var penginapan: Penginapan

    private lateinit var reviewAdapter: ReviewPenginapanAdapter
    private lateinit var hotelFacilityAdapter: HotelFacilityAdapter
    lateinit var pref: SharedPreferenceManager
    private var token: String = ""
    private var email: String = ""
    var isFavorite = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPenginapanDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateAppbar()
        val safeArgs = arguments?.let { PenginapanDetailFragmentArgs.fromBundle(it) }
        val uuid = safeArgs?.uuidPenginapan ?: ""

        pref = SharedPreferenceManager(requireContext())
        token = pref.getToken ?: ""
        email = pref.getEmail ?: ""

        Timber.d("Token is $token")


        initiateRecyclerViews()
        initiateDetailData(uuid)
        initiateUlasanData(uuid)
    }

    private fun initiateDetailData(uuid: String) {
        viewModel.isAlreadyFavorite(uuid, email).observe(viewLifecycleOwner, {
            Timber.d("Favorite is $it")
            isFavorite = it
            getDetailData(uuid)
        })
    }

    private fun getDetailData(uuid: String) {
        viewModel.getPenginapanDetail(uuid).observe(viewLifecycleOwner, { response ->
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
                    penginapan = response.data
                    loadUiDetailPenginapan(response.data)
                    showLoading(false)
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
    }

    private fun initiateRecyclerViews() {
        reviewAdapter = ReviewPenginapanAdapter()
        binding.rvUlasan.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUlasan.adapter = reviewAdapter

        hotelFacilityAdapter = HotelFacilityAdapter()
        with(binding.rvHotelFacility) {
            adapter = hotelFacilityAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUiDetailPenginapan(penginapan: Penginapan) {
        binding.tvPenginapanName.text = penginapan.name
        binding.tvPenginapanLocation.text = penginapan.address
        binding.tvPenginapanDescription.text = penginapan.description
        binding.hotelClassStar.rating = penginapan.hotelStar?.toFloat() ?: 0f
        binding.tvLanguage.text = penginapan.language
        binding.tvPriceRange.text = "Rp ${penginapan.priceMin} - Rp ${penginapan.priceMax}"
        binding.penginapanStar.rating = penginapan.ratingAverage?.toFloat() ?: 0f
        binding.penginapanServiceStar.rating = penginapan.ratingService?.toFloat() ?: 0f
        binding.penginapanFriendlyStar.rating = penginapan.ratingFriendly?.toFloat() ?: 0f
        binding.penginapanCleanStar.rating = penginapan.ratingClean?.toFloat() ?: 0f

        binding.tvPenginapanAccreditation.text =
            if (penginapan.ratingAverage != null) penginapan.ratingAverage?.toJelajahinAccreditation() else (0.0).toJelajahinAccreditation()

        if (penginapan.ratingAverage.toString().length > 3) {
            val maxLength: Int = min(penginapan.ratingAverage.toString().length, 3)
            binding.tvPenginapanRating.text =
                if (penginapan.ratingAverage == null) "0.0" else penginapan.ratingAverage.toString()
                    .substring(0, maxLength)
        } else {
            binding.tvPenginapanRating.text =
                if (penginapan.ratingAverage == null) "0.0" else penginapan.ratingAverage.toString()
        }

        binding.btnBookmark.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (!isFavorite) R.color.grey_600 else R.color.dusk_yellow
            )
        )


        view?.let {
            Glide.with(it)
                .load(JelajahinConstValues.BASE_URL + penginapan.imageUrl)
                .into(binding.imgPenginapan)
        }

        binding.btnTambahUlasan.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                if (token.isNotEmpty()) {
                    navigateToAddUlasan()
                } else {
                    requireContext().showOKDialog(
                        "Akses Ditolak!",
                        "Kamu harus memiliki akun JelajahIn jika ingin memberikan penginapan ini sebuah ulasan!"
                    )
                }
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        initiateContactView(penginapan)

        val facilities: List<String> = penginapan.hotelFacility.split(",")
        hotelFacilityAdapter.setData(facilities)

        binding.btnBookmark.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                if (!isFavorite) {
                    val favoriteEntity = FavoriteEntity(
                        uuid = penginapan.uuidPenginapan,
                        name = penginapan.name,
                        address = penginapan.address,
                        favoriteType = TypeUtils.FAVORITE_PENGINAPAN,
                        ratingAvg = penginapan.ratingAverage ?: 0.0,
                        ratingCount = penginapan.ratingCount ?: 0,
                        imageUrl = penginapan.imageUrl,
                        savedBy = email
                    )
                    saveItemToFavorite(favoriteEntity)
                } else {
                    removeItemFromFavorite(penginapan.uuidPenginapan, email)
                }
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
    }

    private fun saveItemToFavorite(favoriteEntity: FavoriteEntity) {
        viewModel.insertPenginapanToFavorite(favoriteEntity)
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
        viewModel.removePenginapanFromFavorite(uuidWisata, email)
        Timber.d("Removed $uuidWisata with $email")
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
            PenginapanDetailFragmentDirections.actionPenginapanDetailFragmentToUlasanPenginapanFragment(
                penginapan
            )
        findNavController().navigate(action)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun initiateContactView(penginapan: Penginapan){
        when {
            penginapan.website.isNullOrEmpty() && penginapan.phone.isNullOrEmpty() -> {
                binding.contactComponent.root.visibility = View.GONE
            }
            penginapan.website?.isEmpty()!! -> {
                binding.contactComponent.layoutWeb.visibility = View.GONE
                binding.contactComponent.layoutEmail.visibility = View.GONE
            }
            penginapan.phone?.isEmpty()!! -> {
                binding.contactComponent.layoutTelfon.visibility = View.GONE
                binding.contactComponent.layoutEmail.visibility = View.GONE
            }
        }

        binding.contactComponent.layoutWeb.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                val action =
                    PenginapanDetailFragmentDirections.actionPenginapanDetailFragmentToWebViewFragment(penginapan.website ?: "")
                findNavController().navigate(action)
            }, UiConstValue.FAST_ANIMATION_TIME)
        }

        binding.contactComponent.layoutTelfon.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                Timber.d("Phone is not empty")
            }, UiConstValue.FAST_ANIMATION_TIME)
        }
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