package com.ibnu.jelajahin.core.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibnu.jelajahin.core.data.local.entities.FavoriteEntity
import com.ibnu.jelajahin.core.databinding.BookmarkItemBinding
import com.ibnu.jelajahin.core.extention.formatAverageTooLong
import com.ibnu.jelajahin.core.extention.popTap
import com.ibnu.jelajahin.core.ui.adapter.handler.BookmarkItemHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues

class BookmarkAdapter(private val handler: BookmarkItemHandler) :
    RecyclerView.Adapter<BookmarkAdapter.AdsViewHolder>() {

    private val listBookmark = ArrayList<FavoriteEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listFacility: List<FavoriteEntity>) {
        this.listBookmark.addAll(listFacility)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding =
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        val data = listBookmark[position]
        holder.bind(data, position)
        holder.itemView.setOnClickListener {
            handler.onItemClicked(data.uuid, data.favoriteType)
        }
    }

    override fun getItemCount(): Int = listBookmark.size

    inner class AdsViewHolder(private val binding: BookmarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: FavoriteEntity, position: Int) {
            binding.rbRestaurant.rating = bookmark.ratingAvg.toFloat()
            binding.tvBookmarName.text = bookmark.name
            binding.tvBookmarLocation.text = bookmark.address
            binding.tvBookmarRating.text
            if (bookmark.ratingAvg.toString().length > 3) {
                binding.tvBookmarRating.text = bookmark.ratingAvg.toString().formatAverageTooLong()
            } else {
                binding.tvBookmarRating.text = bookmark.ratingAvg.toString()
            }

            binding.btnUnBookmark.setOnClickListener {
                it.popTap()
                Handler(Looper.getMainLooper()).postDelayed({
                    deleteBookmarkDialog(binding.root.context, bookmark.uuid, position)
                }, 200)
            }

            Glide.with(itemView)
                .load(JelajahinConstValues.BASE_URL_IMAGE + bookmark.imageUrl)
                .into(binding.imvBookmark)
        }
    }

    private fun deleteBookmarkDialog(context: Context, uuid: String, position: Int) {
        AlertDialog.Builder(context).apply {
            setTitle("Hapus")
            setMessage("Apakah Anda yakin untuk menghapus item ini dari bookmark?")
            setNegativeButton("Tidak") { p0, _ ->
                p0.dismiss()
            }
            setPositiveButton("IYA") { _, _ ->
                handler.onUnBookmarkClick(uuid)
                removeAt(position)
            }
        }.create().show()
    }

    private fun removeAt(position: Int) {
        listBookmark.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listBookmark.size)
    }
}