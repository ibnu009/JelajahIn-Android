package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.response.WisataDetailResponse
import com.ibnu.jelajahin.core.data.remote.response.WisataResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WisataService {

    @GET("api/wisata")
    suspend fun getWisataByProvinceAndCity(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int
    ): WisataResponse

    @GET("api/wisata/search")
    suspend fun searchWisata(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int,
        @Query("search") searchQuery: String?
    ): WisataResponse

    @GET("api/wisata/locations")
    suspend fun getWisataLocations(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int
    ): WisataResponse

    @GET("api/wisata/{uuidWisata}")
    suspend fun getWisataDetail(
        @Path("uuidWisata") uuidWisata: String,
    ): WisataDetailResponse
}