package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Review
import com.ibnu.jelajahin.core.databinding.UlasanItemBinding
import com.ibnu.jelajahin.core.utils.JelajahinConstValues

class ReviewWisataAdapter() :
    RecyclerView.Adapter<ReviewWisataAdapter.UlasanViewHolder>() {

    private val listReview = ArrayList<Review>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listReview: List<Review>) {
        this.listReview.addAll(listReview)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UlasanViewHolder {
        val binding = UlasanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UlasanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UlasanViewHolder, position: Int) {
        val ads = listReview[position]
        holder.bind(ads)
    }

    override fun getItemCount(): Int = listReview.size

    inner class UlasanViewHolder(val binding: UlasanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {

            binding.tvUlasanTitle.text = review.title
            binding.tvUlasanContent.text = review.content

            Glide.with(binding.root.context)
                .load("${JelajahinConstValues.BASE_URL}${review.imageUrl}")
                .placeholder(R.color.input_color)
                .into(binding.imgPersonUlasan)
        }
    }

}