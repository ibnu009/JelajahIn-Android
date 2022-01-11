package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.data.model.Shop
import com.ibnu.jelajahin.core.databinding.ShopItemBinding
import com.ibnu.jelajahin.core.ui.adapter.handler.ShopItemHandler

class ShopAdapter(private val handler: ShopItemHandler) :
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    private val listShop = ArrayList<Shop>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listShop: List<Shop>) {
        this.listShop.addAll(listShop)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding =
            ShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val data = listShop[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            handler.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int = listShop.size

    @SuppressLint("SetTextI18n")
    inner class ShopViewHolder(private val binding: ShopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shop: Shop) {
            binding.tvPrice.text = " ${shop.price} pts"
            binding.tvProductName.text = shop.name

            Glide.with(itemView)
                .load(shop.imageUrl)
                .into(binding.imvItem)
        }
    }

}