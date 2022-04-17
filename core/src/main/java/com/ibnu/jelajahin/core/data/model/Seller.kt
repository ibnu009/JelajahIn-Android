package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Seller(
    @field:SerializedName("id_seller")
    val id: String,
    val name: String,
    val address: String,
    @field:SerializedName("image_url")
    val imageUrl: String,
    @field:SerializedName("price_min")
    val priceMin: String,
    @field:SerializedName("price_max")
    val priceMax: String,
    val latitude: Double,
    val longitude: Double
)
