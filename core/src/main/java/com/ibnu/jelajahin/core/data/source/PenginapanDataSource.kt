package com.ibnu.jelajahin.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.Penginapan
import com.ibnu.jelajahin.core.data.model.Review
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.network.PenginapanService
import com.ibnu.jelajahin.core.data.source.factory.PenginapanPagingFactory
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PenginapanDataSource @Inject constructor(
    private val service: PenginapanService
) {

    fun getStreamPenginapans(
        provinceId: Int,
        searchQuery: String
    ): Flow<PagingData<Penginapan>> {
        return Pager(
            config = PagingConfig(
                pageSize = JelajahinConstValues.DEFAULT_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { PenginapanPagingFactory(service, provinceId, searchQuery) }
        ).flow
    }

    suspend fun getPenginapanLocations(
        provinceId: Int,
    ): Flow<ApiResponse<List<Penginapan>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getPenginapan(provinceId, searchQuery = null)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.listPenginapan))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPenginapanById(uuid: String): Flow<ApiResponse<Penginapan>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getPenginapanDetail(uuid)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.penginapan))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Event $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPenginapanRecommendation(): Flow<ApiResponse<List<Penginapan>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getPenginapanRecommendation()
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.listPenginapan))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Penginapan Ulasan $e")
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getReviewPenginapan(uuid: String): Flow<ApiResponse<List<Review>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getUlasanPenginapan(uuid)
                if (response.rowCount > 0) {
                    emit(ApiResponse.Success(response.reviews))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Timber.e("Error Penginapan Ulasan $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}