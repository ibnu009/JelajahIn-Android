package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.response.GemDetailResponse
import com.ibnu.jelajahin.core.data.remote.response.GemResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface DiscoveryService {

    @GET("api/gem")
    suspend fun getAllGems(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int,
    ): GemResponse

    @GET("api/gem/{uuidGem}")
    suspend fun getGemDetail(
        @Path("uuidGem") uuidGem: String,
    ): GemDetailResponse

}