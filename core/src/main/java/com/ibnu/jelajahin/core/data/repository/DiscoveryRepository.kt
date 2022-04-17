package com.ibnu.jelajahin.core.data.repository

import com.ibnu.jelajahin.core.data.model.Gem
import com.ibnu.jelajahin.core.data.model.Memorial
import com.ibnu.jelajahin.core.data.model.Typical
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.source.DiscoveryDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoveryRepository @Inject constructor(
    private val discoveryDataSource: DiscoveryDataSource
) {

    suspend fun getAllGems(provinceId: Int, cityId: Int): Flow<ApiResponse<List<Gem>>> {
        return discoveryDataSource.getAllGems(provinceId, cityId)
    }

    suspend fun getGemDetail(gemUuid: String): Flow<ApiResponse<Gem>> {
        return discoveryDataSource.getGemDetail(gemUuid)
    }

    suspend fun getAllTypical(provinceId: Int, cityId: Int): Flow<ApiResponse<List<Typical>>> {
        return discoveryDataSource.getAllTypical(provinceId, cityId)
    }

    suspend fun getTypicalDetail(uuidTypical: String): Flow<ApiResponse<Typical>> {
        return discoveryDataSource.getTypicalDetail(uuidTypical)
    }

    suspend fun getAllMemorials(provinceId: Int, cityId: Int): Flow<ApiResponse<List<Memorial>>> {
        return discoveryDataSource.getAllMemorial(provinceId, cityId)
    }

    suspend fun getMemorialDetail(uuidMemorial: String): Flow<ApiResponse<Memorial>> {
        return discoveryDataSource.getMemorialDetail(uuidMemorial)
    }
}