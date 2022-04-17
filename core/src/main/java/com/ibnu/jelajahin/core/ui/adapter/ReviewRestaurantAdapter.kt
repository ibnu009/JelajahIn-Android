package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Review
import com.ibnu.jelajahin.core.databinding.UlasanRestaurantItemBinding
import com.ibnu.jelajahin.core.utils.JelajahinConstValues

class ReviewRestaurantAdapter :
    RecyclerView.Adapter<ReviewRestaurantAdapter.UlasanViewHolder>() {

    private val listReview = ArrayList<Review>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listReview: List<Review>) {
        this.listReview.addAll(listReview)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UlasanViewHolder {
        val binding = UlasanRestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UlasanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UlasanViewHolder, position: Int) {
        val ads = listReview[position]
        holder.bind(ads)
    }

    override fun getItemCount(): Int = listReview.size

    @SuppressLint("SetTextI18n")
    inner class UlasanViewHolder(val binding: UlasanRestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {

            binding.ratingBarService.rating = review.ratingService.toFloat()
            binding.ratingBarFood.rating = review.ratingFood.toFloat()
            binding.ratingBarClean.rating = review.ratingClean.toFloat()

            binding.tvUlasanTitle.text = review.title
            binding.tvUlasanContent.text = review.content
            binding.tvReviewerName.text = "-" + review.reviewerName


            Glide.with(binding.root.context)
                .load("${JelajahinConstValues.BASE_URL_IMAGE}${review.reviewerProfileImage}")
                .placeholder(R.color.input_color)
                .into(binding.imgPersonUlasan)

            Glide.with(binding.root.context)
                .load("${JelajahinConstValues.BASE_URL}${review.imageUrl}")
                .placeholder(R.color.input_color)
                .into(binding.imgReview)

        }
    }

}