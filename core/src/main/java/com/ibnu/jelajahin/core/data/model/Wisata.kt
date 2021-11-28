package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Wisata(
    @field:SerializedName("uuid_wisata")
    val uuidWisata: String,
    val name: String,
    val description: String,
    @field:SerializedName("image_url")
    val imageUrl: String,
    @field:SerializedName("ticket_price")
    val ticketPrice: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    @field:SerializedName("rating_avg")
    val ratingAverage: Double,
    @field:SerializedName("rating_count")
    val ratingCount: Int,
    @field:SerializedName("province_id")
    val provinceID: Int,
    @field:SerializedName("province_name")
    val provinceName: String,
    @field:SerializedName( "city_id")
    val cityID: Int,
    @field:SerializedName("city_name")
    val cityName: String,
)
