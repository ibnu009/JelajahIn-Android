package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.databinding.RecommendationItemBinding
import com.ibnu.jelajahin.core.ui.adapter.handler.RecommendationItemClickHandler
import com.ibnu.jelajahin.core.ui.adapter.handler.RecyclerviewItemClickHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.RECOMMENDATION_RESTAURANT_TYPE

class RecommendedRestaurantAdapter(private val onClickAction: RecommendationItemClickHandler) :
    RecyclerView.Adapter<RecommendedRestaurantAdapter.RecommendationViewHolder>() {

    private val listRestaurant = ArrayList<Restaurant>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listRestaurant: List<Restaurant>) {
        this.listRestaurant.addAll(listRestaurant)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding =
            RecommendationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val data = listRestaurant[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onClickAction.onItemClicked(data.uuidRestaurant, RECOMMENDATION_RESTAURANT_TYPE)
        }
    }

    override fun getItemCount(): Int = if (listRestaurant.size > 5) 5 else listRestaurant.size

    @SuppressLint("SetTextI18n")
    inner class RecommendationViewHolder(val binding: RecommendationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            binding.tvTitle.text = restaurant.name
            binding.tvRecommendationLocation.text = restaurant.address
            binding.recommendationRating.rating = restaurant.ratingAverage?.toFloat() ?: 0f
            binding.tvRecommendationRating.text = if (restaurant.ratingAverage == null) "0.0" else restaurant.ratingAverage.toString()
            binding.recommendationRatingTotal.text = if (restaurant.ratingCount == null) "(0)" else "(${restaurant.ratingCount})"

            Glide.with(binding.root.context)
                .load("${JelajahinConstValues.BASE_URL}${restaurant.imageUrl}")
                .placeholder(R.color.input_color)
                .into(binding.imgCover)
        }
    }
}