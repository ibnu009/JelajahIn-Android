package com.ibnu.jelajahin.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.databinding.RestaurantItemBinding
import com.ibnu.jelajahin.core.ui.adapter.handler.RecyclerviewItemClickHandler

class RestaurantAdapter(private val onClickAction: RecyclerviewItemClickHandler) :
    PagingDataAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        getItem(position)?.let { restaurant ->
            holder.bind(restaurant)
            holder.itemView.setOnClickListener {
                onClickAction.onItemClicked(restaurant.uuidRestaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding =
            RestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    inner class RestaurantViewHolder(private val binding: RestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            binding.tvRestaurantName.text = restaurant.name
            binding.tvRestaurantLocation.text = restaurant.address
            binding.tvRestaurantType.text = restaurant.restaurantType
            binding.rbRestaurant.rating = restaurant.ratingAverage?.toFloat() ?: 0f
            binding.tvRestaurantRating.text = if (restaurant.ratingAverage != null) restaurant.ratingAverage.toString() else "0.0"

            Glide.with(itemView)
                .load(restaurant.imageUrl)
                .into(binding.imvRestaurant)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Restaurant> =
            object : DiffUtil.ItemCallback<Restaurant>() {
                override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                    return oldItem.uuidRestaurant == newItem.uuidRestaurant && oldItem.uuidRestaurant == newItem.uuidRestaurant
                }

                override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                    return oldItem == newItem
                }
            }
    }

}

