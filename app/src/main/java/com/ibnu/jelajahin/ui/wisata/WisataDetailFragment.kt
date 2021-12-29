package com.ibnu.jelajahin.ui.wisata

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
import com.google.android.material.appbar.AppBarLayout
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.toJelajahinAccreditation
import com.ibnu.jelajahin.core.ui.adapter.AdsAdapter
import com.ibnu.jelajahin.core.ui.adapter.ReviewWisataAdapter
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL
import com.ibnu.jelajahin.databinding.FragmentWisataDetailBinding
import com.ibnu.jelajahin.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WisataDetailFragment : Fragment() {

    private val viewModel: WisataViewModel by viewModels()

    private var _binding: FragmentWisataDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var wisata: Wisata
    var isFavorite = false

    private lateinit var reviewAdapter: ReviewWisataAdapter

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

        initiateRecyclerViews()
        initiateDetailData(uuidWisata)
        initiateUlasanData(uuidWisata)

        binding.refresh.setOnRefreshListener {
            initiateDetailData(uuidWisata)
        }

    }

    private fun initiateDetailData(uuidWisata: String) {
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
        binding.tvWisataRating.text =
            if (wisata.ratingAverage == 0.0) "0.0" else wisata.ratingAverage.toString()
        view?.let {
            Glide.with(it)
                .load(BASE_URL+wisata.imageUrl)
                .into(binding.imgWisata)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToAddUlasan() {
        val action =
            WisataDetailFragmentDirections.actionWisataDetailFragmentToUlasanWisataFragment(wisata)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}