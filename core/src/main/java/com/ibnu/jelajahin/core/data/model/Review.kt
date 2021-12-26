package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int,
    @field:SerializedName("ulasan_title")
    val title: String,
    @field:SerializedName("ulasan_content")
    val content: String,
    val rating: Int,
    @field:SerializedName("image_url")
    val imageUrl: String,
)
