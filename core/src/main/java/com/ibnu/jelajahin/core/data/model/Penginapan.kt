package com.ibnu.jelajahin.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Penginapan(
    @field:SerializedName("uuid_penginapan")
    val uuidPenginapan: String,
    val name: String,
    val description: String,
    @field:SerializedName("price_min")
    val priceMin: String,
    @field:SerializedName("price_max")
    val priceMax: String,
    @field:SerializedName("hotel_facility")
    val hotelFacility: String,
    @field:SerializedName("hotel_star")
    val hotelStar: Int?,
    @field:SerializedName("language")
    val language: String,
    @field:SerializedName("phone_number")
    val phone: String?,
    @field:SerializedName("website_url")
    val website: String?,
    @field:SerializedName("image_url")
    val imageUrl: String,
    val address: String,
    @field:SerializedName("rating_avg")
    val ratingAverage: Double?,
    @field:SerializedName("rating_service")
    val ratingService: Double?,
    @field:SerializedName("rating_friendly")
    val ratingFriendly: Double?,
    @field:SerializedName("rating_clean")
    val ratingClean: Double?,
    @field:SerializedName("rating_count")
    val ratingCount: Int?,
    @field:SerializedName("provinsi_id")
    val provinceID: Int,
    @field:SerializedName("kabupaten_id")
    val cityID: Int,
    val latitude: Double,
    val longitude: Double,
) : Parcelable