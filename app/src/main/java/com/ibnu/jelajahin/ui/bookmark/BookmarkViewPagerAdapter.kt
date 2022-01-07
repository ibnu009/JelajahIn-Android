package com.ibnu.jelajahin.ui.bookmark

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils.FAVORITE_PENGINAPAN
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils.FAVORITE_RESTAURANT
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils.FAVORITE_WISATA

class BookmarkViewPagerAdapter(
    fa: FragmentActivity,
    private val email: String
) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                BookmarkContentFragment(FAVORITE_WISATA, email)
            }
            1 -> {
                BookmarkContentFragment(FAVORITE_PENGINAPAN, email)
            }
            2 -> {
                BookmarkContentFragment(FAVORITE_RESTAURANT, email)
            }
            else -> BookmarkContentFragment(FAVORITE_WISATA, email)
        }
    }
}