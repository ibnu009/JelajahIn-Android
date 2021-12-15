package com.ibnu.jelajahin.core.data.source

import com.ibnu.jelajahin.core.data.model.Gem
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.network.DiscoveryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoveryDataSource @Inject constructor(
    private val discoveryService: DiscoveryService
) {

    suspend fun getAllGems(provinceId: Int, cityId: Int): Flow<ApiResponse<List<Gem>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = discoveryService.getAllGems(provinceId, cityId)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.gems))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getGemDetail(eventGem: String): Flow<ApiResponse<Gem>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = discoveryService.getGemDetail(eventGem)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.gem))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

}