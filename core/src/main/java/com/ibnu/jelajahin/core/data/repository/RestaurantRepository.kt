package com.ibnu.jelajahin.core.data.repository

import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.model.Review
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.source.RestaurantDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepository @Inject constructor(
    private val datasource: RestaurantDataSource
) {

    fun getRestaurant(provinceId: Int, searchQuery: String): Flow<PagingData<Restaurant>> {
        return datasource.getStreamRestaurants(provinceId, searchQuery)
    }

    suspend fun getRestaurantDetail(uuid: String): Flow<ApiResponse<Restaurant>> {
        return datasource.getRestaurantById(uuid)
    }

    suspend fun getRestaurantLocations(
        provinceId: Int,
    ): Flow<ApiResponse<List<Restaurant>>> {
        return datasource.getRestaurantLocations(provinceId)
    }

    suspend fun getRestaurantRecommendation(): Flow<ApiResponse<List<Restaurant>>> {
        return datasource.getRestaurantRecommendation()
    }

    suspend fun getReviewRestaurant(uuid: String): Flow<ApiResponse<List<Review>>> {
        return datasource.getReviewRestaurant(uuid)
    }

    suspend fun checkUserReviewStatus(token: String, uuid: String): Flow<String> {
        return datasource.getUserReviewStatus(token, uuid)
    }
}