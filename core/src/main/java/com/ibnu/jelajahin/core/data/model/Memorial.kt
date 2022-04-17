package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Memorial(
    @field:SerializedName("uuid_memorial")
    val uuid: String,
    val name: String,
    val description: String,
    val address: String,
    @field:SerializedName("image_url")
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    @field:SerializedName("province_id")
    val provinceId: Long,
    @field:SerializedName("city_id")
    val cityId: Long,
    @field:SerializedName("xp_reward")
    val xpReward: Int
)
