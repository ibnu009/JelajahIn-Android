package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Gem (
    @field:SerializedName("uuid_gem")
    val uuidGem: String,
    val name: String,
    val description: String,
    @field:SerializedName("image_url")
    val imageURL: String,
    @field:SerializedName("xp_reward")
    val xpReward: Int,
    @field:SerializedName("latitude")
    val latitude: Double,
    @field:SerializedName("longitude")
    val longitude: Double,
    @field:SerializedName("province_id")
    val provinceID: Int,
    @field:SerializedName( "city_id")
    val cityID: Int,
)