package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class PointHistory(
    val id: Int,
    @field:SerializedName("quantity_points")
    val quantityPoint: Int,
    @field:SerializedName("quantity_xp")
    val quantityXp: Int,
    val type: Int,
    @field:SerializedName("created_date")
    val createdDate: String,
)
