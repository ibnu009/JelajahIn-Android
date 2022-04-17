package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.response.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface DiscoveryService {

    @GET("api/discovery/gem")
    suspend fun getAllGems(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int,
    ): GemResponse

    @GET("api/discovery/gem/{uuidGem}")
    suspend fun getGemDetail(
        @Path("uuidGem") uuidGem: String,
    ): GemDetailResponse

    @GET("api/discovery/typical")
    suspend fun getAllTypical(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int,
    ): TypicalResponse

    @GET("api/discovery/typical/{uuidTypical}")
    suspend fun getTypicalDetail(
        @Path("uuidTypical") uuidTypical: String,
    ): TypicalDetailResponse

    @GET("api/discovery/memorial")
    suspend fun getAllMemorial(
        @Query("provinceId") provinceId: Int,
        @Query("cityId") cityId: Int,
    ): MemorialResponse

    @GET("api/discovery/memorial/{uuidMemorial}")
    suspend fun getMemorialDetail(
        @Path("uuidMemorial") uuidMemorial: String,
    ): MemorialDetailResponse

}