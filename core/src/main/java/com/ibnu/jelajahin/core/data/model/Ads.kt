package com.ibnu.jelajahin.core.data.model

import com.google.gson.annotations.SerializedName

data class Ads(
    @field:SerializedName("uuid_ads")
    val uuidAds: String,
    val sequence: Int,
    @field:SerializedName("action_type")
    val actionType: String,
    @field:SerializedName("action_value")
    val actionValue: String?,
    @field:SerializedName("action_param")
    val actionParams: String?,
    @field:SerializedName("image_url")
    val imageURL: String,
    @field:SerializedName("province_id")
    val provinceID: Int,
)
