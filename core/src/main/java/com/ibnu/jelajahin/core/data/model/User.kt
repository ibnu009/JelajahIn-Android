package com.ibnu.jelajahin.core.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @field:SerializedName( "uuid_user ")
    val uuidUser: String,
    @field:SerializedName( "full_name")
    val fullName: String,
    val email: String,
    @field:SerializedName( "image_url")
    val imageUrl: String,
    val origin: String,
    @field:SerializedName("total_appreciation")
    val totalAppreciations: Int,
    @field:SerializedName("total_reviews")
    val totalReviews: Int,
    @field:SerializedName("total_events")
    val totalEvents: Int,
    @field:SerializedName("total_points")
    val totalPoints: Int,
    @field:SerializedName("total_xp")
    val totalXp: Int
) : Parcelable
