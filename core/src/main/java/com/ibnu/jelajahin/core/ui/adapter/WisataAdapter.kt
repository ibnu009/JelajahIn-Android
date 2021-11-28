package com.ibnu.jelajahin.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.databinding.WisataItemBinding

class WisataAdapter(private val onClickAction: RecyclerviewItemClickHandler) :
    PagingDataAdapter<Wisata, WisataAdapter.WisataViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: WisataViewHolder, position: Int) {
        getItem(position)?.let { wisata ->
            holder.bind(wisata)
            holder.itemView.setOnClickListener {
                onClickAction.onItemClicked(wisata.uuidWisata)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WisataViewHolder {
        val binding = WisataItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WisataViewHolder(binding)
    }

    inner class WisataViewHolder(private val binding: WisataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wisata: Wisata) {
            binding.tvWisataName.text = wisata.name
            binding.tvWisataLocation.text = wisata.address
            binding.rbRestaurant.rating = wisata.ratingAverage.toFloat()
            binding.tvWisataRating.text = wisata.ratingAverage.toString()

            Glide.with(itemView)
                .load(wisata.imageUrl)
                .into(binding.imvWisata)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Wisata> =
            object : DiffUtil.ItemCallback<Wisata>() {
                override fun areItemsTheSame(oldItem: Wisata, newItem: Wisata): Boolean {
                    return oldItem.uuidWisata == newItem.uuidWisata && oldItem.uuidWisata == newItem.uuidWisata
                }

                override fun areContentsTheSame(oldItem: Wisata, newItem: Wisata): Boolean {
                    return oldItem == newItem
                }
            }
    }

}

