package com.ibnu.jelajahin.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.network.WisataService
import com.ibnu.jelajahin.core.data.source.factory.WisataPagingFactory
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WisataDataSource@Inject constructor(
    private val wisataService: WisataService
) {

    fun getStreamWisataByProvinceAndCity(provinceId: Int, cityId: Int): Flow<PagingData<Wisata>> {
        return Pager(
            config = PagingConfig(
                pageSize = JelajahinConstValues.DEFAULT_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { WisataPagingFactory(wisataService, provinceId, cityId, null) }
        ).flow
    }

    fun getStreamWisataSearch(provinceId: Int, cityId: Int, searchQuery: String): Flow<PagingData<Wisata>> {
        return Pager(
            config = PagingConfig(
                pageSize = JelajahinConstValues.DEFAULT_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { WisataPagingFactory(wisataService, provinceId, cityId, searchQuery) }
        ).flow
    }

    suspend fun getWisataLocations(provinceId: Int, cityId: Int): Flow<ApiResponse<List<Wisata>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = wisataService.getWisataLocations(provinceId, cityId)
                if (response.rowCount > 0){
                    emit(ApiResponse.Success(response.wisata))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getWisataById(wisataUuid: String): Flow<ApiResponse<Wisata>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = wisataService.getWisataDetail(wisataUuid)
                if (response.rowCount > 0){
                    emit(ApiResponse.Success(response.wisata))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}