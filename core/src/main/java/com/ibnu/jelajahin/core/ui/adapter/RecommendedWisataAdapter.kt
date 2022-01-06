package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.databinding.RecommendationItemBinding
import com.ibnu.jelajahin.core.ui.adapter.handler.RecommendationItemClickHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.RECOMMENDATION_WISATA_TYPE

class RecommendedWisataAdapter(private val onClickAction: RecommendationItemClickHandler) :
    RecyclerView.Adapter<RecommendedWisataAdapter.RecommendationViewHolder>() {

    private val listWisata = ArrayList<Wisata>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listWisata: List<Wisata>) {
        this.listWisata.addAll(listWisata)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding =
            RecommendationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val data = listWisata[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onClickAction.onItemClicked(data.uuidWisata, RECOMMENDATION_WISATA_TYPE)
        }
    }

    override fun getItemCount(): Int = if (listWisata.size > 5) 5 else listWisata.size

    @SuppressLint("SetTextI18n")
    inner class RecommendationViewHolder(val binding: RecommendationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wisata: Wisata) {
            binding.tvTitle.text = wisata.name
            binding.tvRecommendationLocation.text = wisata.address
            binding.recommendationRating.rating = wisata.ratingAverage.toFloat()
            binding.tvRecommendationRating.text = wisata.ratingAverage.toString()
            binding.recommendationRatingTotal.text = "(${wisata.ratingCount})"

            Glide.with(binding.root.context)
                .load("${JelajahinConstValues.BASE_URL}${wisata.imageUrl}")
                .placeholder(R.color.input_color)
                .into(binding.imgCover)
        }
    }
}