package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.response.GenericResponse
import com.ibnu.jelajahin.core.data.remote.response.ReviewResponse
import com.ibnu.jelajahin.core.data.remote.response.WisataDetailResponse
import com.ibnu.jelajahin.core.data.remote.response.WisataResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WisataService {

    @GET("api/wisata")
    suspend fun getWisataByProvinceAndCity(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int,
        @Query("search") searchQuery: String?
    ): WisataResponse

    @GET("api/wisata/recommended")
    suspend fun getWisataRecommendation(): WisataResponse

    @GET("api/wisata/locations")
    suspend fun getWisataLocations(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int
    ): WisataResponse

    @GET("api/wisata/{uuidWisata}")
    suspend fun getWisataDetail(
        @Path("uuidWisata") uuidWisata: String,
    ): WisataDetailResponse

    @GET("api/wisata/ulasan/{uuidWisata}")
    suspend fun getUlasanWisata(
        @Path("uuidWisata") uuidWisata: String,
    ): ReviewResponse

    @GET("api/wisata/review_status/{uuidWisata}")
    suspend fun checkUserReviewStatus(
        @Header("token") token: String,
        @Path("uuidWisata") uuidWisata: String,
    ): GenericResponse
}