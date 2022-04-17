package com.ibnu.jelajahin.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.data.model.Penginapan
import com.ibnu.jelajahin.core.databinding.PenginapanItemBinding
import com.ibnu.jelajahin.core.extention.formatAverageTooLong
import com.ibnu.jelajahin.core.ui.adapter.handler.RecyclerviewItemClickHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL_IMAGE

class PenginapanAdapter(private val onClickAction: RecyclerviewItemClickHandler) :
    PagingDataAdapter<Penginapan, PenginapanAdapter.PenginapanViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: PenginapanViewHolder, position: Int) {
        getItem(position)?.let { penginapan ->
            holder.bind(penginapan)
            holder.itemView.setOnClickListener {
                onClickAction.onItemClicked(penginapan.uuidPenginapan)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenginapanViewHolder {
        val binding =
            PenginapanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PenginapanViewHolder(binding)
    }

    inner class PenginapanViewHolder(private val binding: PenginapanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(penginapan: Penginapan) {
            binding.tvPenginapanName.text = penginapan.name
            binding.tvPenginapanLocation.text = penginapan.address
            binding.tvPenginapanType.text = penginapan.hotelFacility
            binding.rbPenginapan.rating = penginapan.ratingAverage?.toFloat() ?: 0f

            if (penginapan.ratingAverage.toString().length > 3) {
                binding.tvPenginapanRating.text =
                    if (penginapan.ratingAverage != null) penginapan.ratingAverage.toString().formatAverageTooLong() else "0.0"
            } else {
                binding.tvPenginapanRating.text =
                    if (penginapan.ratingAverage == null) "0.0" else penginapan.ratingAverage.toString()
            }


            Glide.with(itemView)
                .load(BASE_URL_IMAGE + penginapan.imageUrl)
                .into(binding.imvPenginapan)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Penginapan> =
            object : DiffUtil.ItemCallback<Penginapan>() {
                override fun areItemsTheSame(oldItem: Penginapan, newItem: Penginapan): Boolean {
                    return oldItem.uuidPenginapan == newItem.uuidPenginapan && oldItem.uuidPenginapan == newItem.uuidPenginapan
                }

                override fun areContentsTheSame(oldItem: Penginapan, newItem: Penginapan): Boolean {
                    return oldItem == newItem
                }
            }
    }

}

