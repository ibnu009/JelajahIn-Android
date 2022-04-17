package com.ibnu.jelajahin.core.data.source

import com.ibnu.jelajahin.core.data.model.Gem
import com.ibnu.jelajahin.core.data.model.Memorial
import com.ibnu.jelajahin.core.data.model.Typical
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

    suspend fun getAllTypical(provinceId: Int, cityId: Int): Flow<ApiResponse<List<Typical>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = discoveryService.getAllTypical(provinceId, cityId)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.typical))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTypicalDetail(uuidTypical: String): Flow<ApiResponse<Typical>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = discoveryService.getTypicalDetail(uuidTypical)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.typical))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllMemorial(provinceId: Int, cityId: Int): Flow<ApiResponse<List<Memorial>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = discoveryService.getAllMemorial(provinceId, cityId)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.memorials))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMemorialDetail(uuidMemorial: String): Flow<ApiResponse<Memorial>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = discoveryService.getMemorialDetail(uuidMemorial)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.memorial))
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