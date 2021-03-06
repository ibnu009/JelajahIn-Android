package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.response.EventDetailResponse
import com.ibnu.jelajahin.core.data.remote.response.EventResponse
import com.ibnu.jelajahin.core.data.remote.response.GenericResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET("api/event")
    suspend fun getEventByProvinceAndCity(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int,
        @Query("search") searchQuery: String
    ): EventResponse

    @GET("api/event/{uuidEvent}")
    suspend fun getEventDetail(
        @Path("uuidEvent") uuidEvent: String,
    ): EventDetailResponse

    @GET("api/event/check/attendance_status/{uuidEvent}")
    suspend fun checkUserAttendance(
        @Header("token") token: String,
        @Path("uuidEvent") uuidEvent: String,
        ): GenericResponse

}