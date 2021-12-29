package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int,
    @field:SerializedName("ulasan_title")
    val title: String,
    @field:SerializedName("ulasan_content")
    val content: String,

    val rating: Int,
    @field:SerializedName("rating_service")
    val ratingService: Int,
    @field:SerializedName("rating_food")
    val ratingFood: Int,
    @field:SerializedName("rating_clean")
    val ratingClean: Int,

    @field:SerializedName("image_url")
    val imageUrl: String,
    @field:SerializedName("full_name")
    val reviewerName: String,
    @field:SerializedName("profile_image_url")
    val reviewerProfileImage: String,
)
