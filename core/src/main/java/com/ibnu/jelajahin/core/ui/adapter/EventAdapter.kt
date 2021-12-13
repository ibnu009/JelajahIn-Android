package com.ibnu.jelajahin.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.databinding.EventItemBinding
import com.ibnu.jelajahin.core.extention.parseDateMonthAndYear
import com.ibnu.jelajahin.core.ui.adapter.handler.RecyclerviewItemClickHandler

class EventAdapter(private val onClickAction: RecyclerviewItemClickHandler) :
    PagingDataAdapter<Event, EventAdapter.EventViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let { event ->
            holder.bind(event)
            holder.itemView.setOnClickListener {
                onClickAction.onItemClicked(event.uuidEvent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }


    inner class EventViewHolder(private val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.tvEventName.text = event.name
            binding.tvEventDate.text = event.schedule.parseDateMonthAndYear()
            binding.tvEventLocation.text = "${event.cityName}, ${event.provinceName}"

            Glide.with(itemView)
                .load(event.imageURL)
                .into(binding.imvEvent)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Event> =
            object : DiffUtil.ItemCallback<Event>() {
                override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                    return oldItem.uuidEvent == newItem.uuidEvent && oldItem.uuidEvent == newItem.uuidEvent
                }

                override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

