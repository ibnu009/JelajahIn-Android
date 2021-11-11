package com.ibnu.jelajahin.core.data.remote.response

import com.google.gson.annotations.SerializedName
import com.ibnu.jelajahin.core.data.model.Event

data class EventDetailResponse(
    val status: Int,
    @field:SerializedName( "row_count")
    val rowCount: Int,
    val message: String,
    @field:SerializedName( "data")
    val event: Event
)
