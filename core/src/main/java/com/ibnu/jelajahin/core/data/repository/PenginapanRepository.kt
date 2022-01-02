package com.ibnu.jelajahin.core.data.repository

import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.Penginapan
import com.ibnu.jelajahin.core.data.model.Review
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.source.PenginapanDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PenginapanRepository @Inject constructor(
    private val datasource: PenginapanDataSource
) {

    fun getPenginapan(provinceId: Int, searchQuery: String): Flow<PagingData<Penginapan>> {
        return datasource.getStreamPenginapans(provinceId, searchQuery)
    }

    suspend fun getPenginapanDetail(uuid: String): Flow<ApiResponse<Penginapan>> {
        return datasource.getPenginapanById(uuid)
    }

    suspend fun getPenginapanLocations(
        provinceId: Int,
    ): Flow<ApiResponse<List<Penginapan>>> {
        return datasource.getPenginapanLocations(provinceId)
    }

    suspend fun getReviewPenginapan(uuid: String): Flow<ApiResponse<List<Review>>> {
        return datasource.getReviewPenginapan(uuid)
    }
}