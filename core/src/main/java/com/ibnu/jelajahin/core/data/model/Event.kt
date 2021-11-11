package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Event(

    @field:SerializedName("uuid_event")
    val uuidEvent: String,

    val name: String,
    val description: String,

    @field:SerializedName("image_url")
    val imageURL: String,

    @field:SerializedName("province_id")
    val provinceID: Long,

    @field:SerializedName("province_name")
    val provinceName: String,

    @field:SerializedName( "city_id")
    val cityID: Long,

    @field:SerializedName("city_name")
    val cityName: String,

    val schedule: String,

    @field:SerializedName("ticket_price")
    val ticketPrice: String,

    @field:SerializedName("point_reward")
    val pointReward: Long,

    @field:SerializedName("xp_reward")
    val xpReward: Long,

    val latitude: Double,
    val longtitude: Double,

    @field:SerializedName("is_free")
    val isFree: Long
)
