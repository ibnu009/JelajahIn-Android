package com.ibnu.jelajahin.core.data.remote.network

import com.ibnu.jelajahin.core.data.remote.response.GenericResponse
import com.ibnu.jelajahin.core.data.remote.response.RestaurantDetailResponse
import com.ibnu.jelajahin.core.data.remote.response.RestaurantResponse
import com.ibnu.jelajahin.core.data.remote.response.ReviewResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantService {

    @GET("api/restaurant")
    suspend fun getRestaurant(
        @Query("provinceId") provinceId: Int,
        @Query("search") searchQuery: String?
    ): RestaurantResponse

    @GET("api/restaurant/recommended")
    suspend fun getRestaurantRecommendation(): RestaurantResponse

    @GET("api/restaurant/{uuidRestaurant}")
    suspend fun getRestaurantDetail(
        @Path("uuidRestaurant") uuidRestaurant: String,
    ): RestaurantDetailResponse

    @GET("api/restaurant/ulasan/{uuidRestaurant}")
    suspend fun getUlasanRestaurant(
        @Path("uuidRestaurant") uuidRestaurant: String,
    ): ReviewResponse

    @GET("api/restaurant/review_status/{uuidRestaurant}")
    suspend fun checkUserReviewStatus(
        @Header("token") token: String,
        @Path("uuidRestaurant") uuidRestaurant: String,
    ): GenericResponse

}