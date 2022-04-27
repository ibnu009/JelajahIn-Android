package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class GalleryWisata(
    @field:SerializedName("gallery")
    val gallery: List<ItemGalleryWisata>
)