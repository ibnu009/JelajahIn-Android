package com.ibnu.jelajahin.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.model.Review
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.network.RestaurantService
import com.ibnu.jelajahin.core.data.source.factory.RestaurantPagingFactory
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantDataSource @Inject constructor(
    private val service: RestaurantService
) {

    fun getStreamRestaurants(
        provinceId: Int,
        searchQuery: String
    ): Flow<PagingData<Restaurant>> {
        return Pager(
            config = PagingConfig(
                pageSize = JelajahinConstValues.DEFAULT_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { RestaurantPagingFactory(service, provinceId, searchQuery) }
        ).flow
    }

    suspend fun getRestaurantLocations(
        provinceId: Int,
    ): Flow<ApiResponse<List<Restaurant>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getRestaurant(provinceId, searchQuery = null)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.restaurants))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getRestaurantById(uuid: String): Flow<ApiResponse<Restaurant>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getRestaurantDetail(uuid)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.restaurant))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getRestaurantRecommendation(): Flow<ApiResponse<List<Restaurant>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getRestaurantRecommendation()
                if (response.rowCount > 0){
                    emit(ApiResponse.Success(response.restaurants))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Restaurant Ulasan $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getReviewRestaurant(uuid: String): Flow<ApiResponse<List<Review>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getUlasanRestaurant(uuid)
                if (response.rowCount > 0){
                    emit(ApiResponse.Success(response.reviews))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Restaurant Ulasan $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserReviewStatus(token: String, uuid: String): Flow<String> {
        return flow {
            val response = service.checkUserReviewStatus(token, uuid)
            emit(response.message)
        }.flowOn(Dispatchers.IO)
    }
}