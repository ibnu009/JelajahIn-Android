package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    @field:SerializedName("uuid_restaurant")
    val uuidRestaurant: String,
    val name: String,
    val description: String,
    @field:SerializedName("price_min")
    val priceMin: String,
    @field:SerializedName("price_max")
    val priceMax: String,
    @field:SerializedName("food_type")
    val foodType: String,
    @field:SerializedName("restaurant_type")
    val restaurantType: String,
    val phone: String,
    val website: String,
    @field:SerializedName("business_time_open")
    val OpenTime: String,
    @field:SerializedName("business_time_close")
    val CloseTime: String,
    @field:SerializedName("image")
    val imageUrl: String,
    @field:SerializedName("ticket_price")
    val ticketPrice: String,
    val address: String,
    @field:SerializedName("rating_avg")
    val ratingAverage: Double?,
    @field:SerializedName("rating_count")
    val ratingCount: Int?,
    @field:SerializedName("provinsi_id")
    val provinceID: Int,
    @field:SerializedName("kabupaten_id")
    val cityID: Int,
    val latitude: Double,
    val longtitude: Double,
)