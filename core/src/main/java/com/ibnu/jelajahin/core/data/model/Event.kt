package com.ibnu.jelajahin.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(

    @field:SerializedName("uuid_event")
    val uuidEvent: String,

    val name: String,
    val description: String,

    @field:SerializedName("image_url")
    val imageURL: String,

    @field:SerializedName("province_id")
    val provinceID: Int,

    @field:SerializedName("province_name")
    val provinceName: String,

    @field:SerializedName( "city_id")
    val cityID: Int,

    @field:SerializedName("city_name")
    val cityName: String,

    @field:SerializedName("start_date")
    val startDate: String,

    @field:SerializedName("end_date")
    val endDate: String,

    @field:SerializedName("ticket_price")
    val ticketPrice: String,

    @field:SerializedName("point_reward")
    val pointReward: Int,

    @field:SerializedName("xp_reward")
    val xpReward: Int,

    val latitude: Double,
    val longtitude: Double,

    @field:SerializedName("is_free")
    val isFree: Int
): Parcelable
