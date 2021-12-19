package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.response.RestaurantDetailResponse
import com.ibnu.jelajahin.core.data.remote.response.RestaurantResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantService {

    @GET("api/restaurant")
    suspend fun getRestaurant(
        @Query("provinceId") provinceId: Int,
        @Query("search") searchQuery: String?
    ): RestaurantResponse

    @GET("api/restaurant/{uuidRestaurant}")
    suspend fun getRestaurantDetail(
        @Path("uuidRestaurant") uuidRestaurant: String,
    ): RestaurantDetailResponse
}