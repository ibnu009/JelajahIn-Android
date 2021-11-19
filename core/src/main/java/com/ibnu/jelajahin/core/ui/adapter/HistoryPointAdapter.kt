package com.ibnu.jelajahin.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ibnu.jelajahin.core.data.model.PointHistory
import com.ibnu.jelajahin.core.databinding.HistoryPointItemBinding

class HistoryPointAdapter :
    PagingDataAdapter<PointHistory, HistoryPointAdapter.PointHistoryViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onBindViewHolder(holder: PointHistoryViewHolder, position: Int) {
        getItem(position)?.let { point ->
            holder.bind(point)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointHistoryViewHolder {
        val binding =
            HistoryPointItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PointHistoryViewHolder(binding)
    }

    inner class PointHistoryViewHolder(private val binding: HistoryPointItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(point: PointHistory) {
            binding.tvPointName.text = "Event"
            binding.tvHistoryDate.text = point.createdDate
            binding.tvPointQuantity.text =
                if (point.type == 0) "-" else "+" + " ${point.quantityPoint}"
        }
    }


    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<PointHistory> =
            object : DiffUtil.ItemCallback<PointHistory>() {
                override fun areItemsTheSame(
                    oldItem: PointHistory,
                    newItem: PointHistory
                ): Boolean {
                    return oldItem.id == newItem.id && oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: PointHistory,
                    newItem: PointHistory
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}