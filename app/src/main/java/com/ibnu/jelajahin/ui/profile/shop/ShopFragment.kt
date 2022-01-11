package com.ibnu.jelajahin.ui.profile.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnu.jelajahin.R
import com.ibnu.jelajahin.core.data.model.Shop
import com.ibnu.jelajahin.core.ui.adapter.AdsAdapter
import com.ibnu.jelajahin.core.ui.adapter.EventAdapter
import com.ibnu.jelajahin.core.ui.adapter.ShopAdapter
import com.ibnu.jelajahin.core.ui.adapter.handler.ShopItemHandler
import com.ibnu.jelajahin.databinding.EventDetailFragmentBinding
import com.ibnu.jelajahin.databinding.FragmentShopBinding
import com.ibnu.jelajahin.ui.profile.ProfileFragmentDirections

class ShopFragment : Fragment(), ShopItemHandler {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateRecyclerview()
        initiateData()
    }

    private fun initiateData(){
        val data = getData()
        adapter.setData(data)
    }

    private fun initiateRecyclerview(){
        adapter = ShopAdapter(this)
        binding.rvShop.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvShop.adapter = adapter
    }

    private fun getData() : List<Shop> {
        val result = ArrayList<Shop>()
        val shop = Shop(
            id = 1,
            name = "T-Shirt Adventurer",
            description = "T-Shirt berbahan katun yang cocok banget buat kamu yang suka jalan-jalan dan berpetulang. Membuat aura adventure mu semakin terasa",
            imageUrl = "https://i.pinimg.com/originals/bd/ef/cb/bdefcbc72735f64db17f3250b1e64245.png",
            price = 150000
        )
        result.add(shop)

        val shop1 = Shop(
            id = 2,
            name = "T-Shirt Adventurer",
            description = "T-Shirt berbahan katun yang cocok banget buat kamu yang suka jalan-jalan dan berpetulang. Membuat aura adventure mu semakin terasa",
            imageUrl = "https://i.pinimg.com/originals/bd/ef/cb/bdefcbc72735f64db17f3250b1e64245.png",
            price = 200000
        )
        result.add(shop1)

        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(shop: Shop) {
        val action = ShopFragmentDirections.actionShopFragmentToShopDetailFragment(shop)
        findNavController().navigate(action)
    }

}