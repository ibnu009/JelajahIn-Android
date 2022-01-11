package com.ibnu.jelajahin.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
) : Parcelable
