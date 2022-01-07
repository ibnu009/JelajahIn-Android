package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibnu.jelajahin.core.databinding.FacilityItemBinding

class HotelFacilityAdapter :
    RecyclerView.Adapter<HotelFacilityAdapter.AdsViewHolder>() {

    private val listFacility = ArrayList<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listFacility: List<String>) {
        this.listFacility.addAll(listFacility)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding =
            FacilityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        val text = listFacility[position]
        holder.bind(text)
    }

    override fun getItemCount(): Int = listFacility.size

    inner class AdsViewHolder(private val binding: FacilityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.tvFacility.text = text
        }
    }
}