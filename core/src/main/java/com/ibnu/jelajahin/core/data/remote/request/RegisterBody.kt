package com.ibnu.jelajahin.core.data.remote.request

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @field:SerializedName("full_name")
    val fullName: String,
    val email: String,
    val password: String
)
