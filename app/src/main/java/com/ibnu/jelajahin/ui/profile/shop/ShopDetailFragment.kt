package com.ibnu.jelajahin.ui.profile.shop

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.extention.showOKDialog
import com.ibnu.jelajahin.databinding.FragmentShopBinding
import com.ibnu.jelajahin.databinding.FragmentShopDetailBinding
import com.ibnu.jelajahin.ui.profile.EditProfileFragmentArgs

class ShopDetailFragment : Fragment() {

    private var _binding: FragmentShopDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val safeArgs = arguments?.let { ShopDetailFragmentArgs.fromBundle(it) }
        val shop = safeArgs?.shop!!

        binding.tvProductDesc.text = shop.description
        binding.tvProductName.text = shop.name
        binding.tvProductPrice.text = " ${shop.price} pts"

        Glide.with(this)
            .load(shop.imageUrl)
            .into(binding.imvProduct)

        binding.btnTukar.setOnClickListener {
            requireContext().showOKDialog("Belum tersedia", "Barang ini belum tersedia! coba lagi nanti")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}