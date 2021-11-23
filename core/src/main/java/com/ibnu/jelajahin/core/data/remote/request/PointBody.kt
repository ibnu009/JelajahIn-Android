package com.ibnu.jelajahin.core.data.remote.request

import com.google.gson.annotations.SerializedName

data class PointBody(
    @field:SerializedName("quantity_points")
    val pointQuantity: Int,
    @field:SerializedName("quantity_xp")
    val xpQuantity: Int,
    @field:SerializedName("type")
    val transactionType: Int,
    @field:SerializedName("created_date")
val createdDate: String,
)