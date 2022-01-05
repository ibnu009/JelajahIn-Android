package com.ibnu.jelajahin.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    @field:SerializedName("phone_number")
    val phone: String?,
    @field:SerializedName("website_url")
    val website: String?,
    @field:SerializedName("business_time_open")
    val OpenTime: String,
    @field:SerializedName("business_time_close")
    val CloseTime: String,
    @field:SerializedName("image_url")
    val imageUrl: String,
    val address: String,
    @field:SerializedName("rating_avg")
    val ratingAverage: Double?,
    @field:SerializedName("rating_service")
    val ratingService: Double?,
    @field:SerializedName("rating_food")
    val ratingFood: Double?,
    @field:SerializedName("rating_clean")
    val ratingClean: Double?,
    @field:SerializedName("rating_count")
    val ratingCount: Int?,
    @field:SerializedName("provinsi_id")
    val provinceID: Int,
    @field:SerializedName("kabupaten_id")
    val cityID: Int,
    val latitude: Double,
    val longtitude: Double,
) : Parcelable