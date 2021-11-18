package com.ibnu.jelajahin.core.data.remote.response

data class UserLoginResponse(
    val status: Int,
    val message: String,
    val token: String
)
