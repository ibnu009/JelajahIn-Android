package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.request.HistoryPointBody
import com.ibnu.jelajahin.core.data.remote.request.LoginBody
import com.ibnu.jelajahin.core.data.remote.request.PointBody
import com.ibnu.jelajahin.core.data.remote.request.RegisterBody
import com.ibnu.jelajahin.core.data.remote.response.GenericResponse
import com.ibnu.jelajahin.core.data.remote.response.PointHistoryResponse
import com.ibnu.jelajahin.core.data.remote.response.UserLoginResponse
import com.ibnu.jelajahin.core.data.remote.response.UserResponse
import retrofit2.http.*

interface UserService {

    @GET("api/user/{uuid_user}")
    suspend fun getUserByUuid(
        @Header("token") token: String,
        @Path("uuid_user") uuidUser: String
    ): UserResponse

    @GET("api/profile")
    suspend fun getUserProfile(
        @Header("token") token: String
    ): UserResponse

    @POST("api/user/login")
    suspend fun loginUser(
        @Body request: LoginBody
    ): UserLoginResponse

    @POST("api/user/signup")
    suspend fun registerUser(
        @Body request: RegisterBody
    ): UserLoginResponse

    @GET("api/user/history_point/show")
    suspend fun getUserPointHistory(
        @Header("token") token: String,
    ): PointHistoryResponse

    @POST("api/user/history_point/insert")
    suspend fun insertPointToUserHistory(
        @Header("token") token: String,
        @Body request: HistoryPointBody
    ): GenericResponse

    @PUT("api/user/add_points")
    suspend fun addPointToUser(
        @Header("Token") token: String,
        @Body request: PointBody
    ):GenericResponse


}