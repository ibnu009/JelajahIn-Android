package com.ibnu.jelajahin.ui.wisata

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.extention.toJelajahinAccreditation
import com.ibnu.jelajahin.databinding.FragmentWisataDetailBinding
import com.ibnu.jelajahin.databinding.WisataFragmentBinding
import com.ibnu.jelajahin.ui.event.detail.EventDetailFragmentArgs
import com.ibnu.jelajahin.utils.UiConstValue.FAST_ANIMATION_TIME
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WisataDetailFragment : Fragment() {

    private val viewModel: WisataViewModel by viewModels()

    private var _binding: FragmentWisataDetailBinding? = null
    private val binding get() = _binding!!

    var isFavorite = false;
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

        viewModel.getWisataDetail(uuidWisata).observe(viewLifecycleOwner, { response ->
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
                    loadUiDetailWisata(response.data)
                    showLoading(false)
                }
                else -> {
                    Timber.d("Unknown Error")
                }
            }
        })
    }

    private fun initiateAppbar(){
        binding.toolBar.setNavigationOnClickListener {
            it.popTap()
        }
    }

    private fun loadUiDetailWisata(wisata: Wisata) {
        binding.tvWisataName.text = wisata.name
        binding.tvWisataLocation.text = wisata.address
        binding.tvWisataDescription.text = wisata.description
        binding.tvTicketPrice.text = wisata.ticketPrice
        binding.tvTicketPriceWeekend.text = wisata.ticketPrice
        binding.wisataStar.rating = wisata.ratingAverage.toFloat()
        binding.tvWisataAccreditation.text = wisata.ratingAverage.toJelajahinAccreditation()
        binding.tvWisataRating.text = if (wisata.ratingAverage == 0.0) "0.0" else wisata.ratingAverage.toString()
        view?.let {
            Glide.with(it)
                .load(wisata.imageUrl)
                .into(binding.imgWisata)
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

    private fun showLoading(isLoading: Boolean){
        if (isLoading){

        } else {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}