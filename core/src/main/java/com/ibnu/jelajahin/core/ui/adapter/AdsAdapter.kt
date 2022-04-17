package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.R
import com.ibnu.jelajahin.core.data.model.Ads
import com.ibnu.jelajahin.core.databinding.BannerItemBinding
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.ui.adapter.handler.AdsItemHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.BASE_URL_IMAGE

class AdsAdapter(private val itemHandler: AdsItemHandler) :
    RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {

    private val listAds = ArrayList<Ads>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listAds: List<Ads>) {
        this.listAds.addAll(listAds)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding = BannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        val ads = listAds[position]
        holder.bind(ads)
        holder.itemView.setOnClickListener {
            it.popTap()
            Handler(Looper.getMainLooper()).postDelayed({
                itemHandler.onBannerClicked(
                    ads.actionType,
                    ads.actionValue,
                    ads.actionParams
                )
            }, 200)
        }
    }

    override fun getItemCount(): Int = listAds.size

    inner class AdsViewHolder(private val binding: BannerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ads: Ads) {
            Glide.with(binding.root.context)
                .load("${BASE_URL_IMAGE}${ads.imageURL}")
                .placeholder(R.color.input_color)
                .into(binding.imvBanner)
        }
    }
}