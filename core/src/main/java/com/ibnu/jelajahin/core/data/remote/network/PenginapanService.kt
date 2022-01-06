package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.response.PenginapanDetailResponse
import com.ibnu.jelajahin.core.data.remote.response.PenginapanResponse
import com.ibnu.jelajahin.core.data.remote.response.ReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PenginapanService {

    @GET("api/penginapan")
    suspend fun getPenginapan(
        @Query("provinceId") provinceId: Int,
        @Query("search") searchQuery: String?
    ): PenginapanResponse

    @GET("api/penginapan/recommended")
    suspend fun getPenginapanRecommendation(): PenginapanResponse

    @GET("api/penginapan/{uuidPenginapan}")
    suspend fun getPenginapanDetail(
        @Path("uuidPenginapan") uuidPenginapan: String,
    ): PenginapanDetailResponse

    @GET("api/penginapan/ulasan/{uuidPenginapan}")
    suspend fun getUlasanPenginapan(
        @Path("uuidPenginapan") uuidPenginapan: String,
    ): ReviewResponse
}