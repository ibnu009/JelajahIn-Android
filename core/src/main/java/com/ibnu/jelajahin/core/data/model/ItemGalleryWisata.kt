package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class ItemGalleryWisata(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("image_url")
    val imageUrl: String

)