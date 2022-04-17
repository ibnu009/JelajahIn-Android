package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Typical(
    @field:SerializedName("uuid_typical")
    val uuidTypical: String,
    val name: String,
    val description: String,
    val origin: String,
    @field:SerializedName("total_seller")
    val totalSeller: Int,
    @field:SerializedName("image_url")
    val imageUrl: String,
    val seller: List<Seller>
)
