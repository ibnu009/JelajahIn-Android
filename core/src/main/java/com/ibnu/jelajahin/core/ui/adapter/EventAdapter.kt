package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.databinding.EventItemBinding
import com.ibnu.jelajahin.core.extention.*
import com.ibnu.jelajahin.core.ui.adapter.handler.RecyclerviewItemClickHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL

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

    @SuppressLint("SetTextI18n")
    inner class EventViewHolder(private val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.tvEventName.text = event.name

            setScheduleText(event)
            binding.tvEventLocation.text = "${event.cityName}, ${event.provinceName}"

            Glide.with(itemView)
                .load(BASE_URL + event.imageURL)
                .into(binding.imvEvent)
        }

        private fun setScheduleText(event: Event) {
            val eventStart = event.startDate.parseDateMonthAndYear()
            val eventEnd = event.endDate.parseDateMonthAndYear()

            val isSameMonth = event.startDate.isThisDateIsTheSameMonthAs(event.endDate)
            val isSameDay = event.startDate.isThisDateIsTheSameDayAs(event.endDate)
            val year = event.startDate.parseDateToYearOnly()

            if (isSameMonth) {
                val month = event.startDate.parseDateToMonthOnly()
                val dateStart = event.startDate.parseDateToDateOnly()
                val dateEnd = event.endDate.parseDateToDateOnly()
                val hourStart = event.startDate.parseHour()
                val hourEnd = event.endDate.parseHour()
                if (isSameDay) {
                    binding.tvEventDate.text = "$dateStart $month $year - $hourStart-$hourEnd"
                } else {
                    binding.tvEventDate.text = "$dateStart-$dateEnd $month $year"
                }
            } else {
                binding.tvEventDate.text = "$eventStart-$eventEnd"
            }
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

