package com.ibnu.jelajahin.core.data.remote.response

import com.google.gson.annotations.SerializedName
import com.ibnu.jelajahin.core.data.model.Memorial
import com.ibnu.jelajahin.core.data.model.Typical

data class TypicalResponse(
    val status: Int,
    @field:SerializedName("row_count")
    val rowCount: Int,
    val message: String,
    @field:SerializedName("data")
    val typical: List<Typical>
)
