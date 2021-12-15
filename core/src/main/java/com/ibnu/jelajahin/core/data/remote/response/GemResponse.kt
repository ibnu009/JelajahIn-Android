package com.ibnu.jelajahin.core.data.remote.response

import com.google.gson.annotations.SerializedName
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.model.Gem

data class GemResponse(
    val status: Int,
    @field:SerializedName( "row_count")
    val rowCount: Int,
    val message: String,
    @field:SerializedName( "data")
    val gems: List<Gem>
)
