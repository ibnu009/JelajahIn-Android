package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Penginapan
import com.ibnu.jelajahin.core.databinding.RecommendationItemBinding
import com.ibnu.jelajahin.core.utils.JelajahinConstValues

class RecommendedPenginapanAdapter :
    RecyclerView.Adapter<RecommendedPenginapanAdapter.RecommendationViewHolder>() {

    private val listPenginapan = ArrayList<Penginapan>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listPenginapan: List<Penginapan>) {
        this.listPenginapan.addAll(listPenginapan)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding =
            RecommendationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val data = listPenginapan[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listPenginapan.size

    @SuppressLint("SetTextI18n")
    inner class RecommendationViewHolder(val binding: RecommendationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(penginapan: Penginapan) {
            binding.tvTitle.text = penginapan.name
            binding.tvRecommendationLocation.text = penginapan.address
            binding.recommendationRating.rating = penginapan.ratingAverage?.toFloat() ?: 0f
            binding.tvRecommendationRating.text = if (penginapan.ratingAverage == null) "0.0" else penginapan.ratingAverage.toString()
            binding.recommendationRatingTotal.text = if (penginapan.ratingCount == null) "(0)" else "(${penginapan.ratingCount})"

            Glide.with(binding.root.context)
                .load("${JelajahinConstValues.BASE_URL}${penginapan.imageUrl}")
                .placeholder(R.color.input_color)
                .into(binding.imgCover)
        }
    }
}