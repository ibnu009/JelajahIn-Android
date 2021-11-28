package com.ibnu.jelajahin.core.data.repository

import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.source.EventDataSource
import com.ibnu.jelajahin.core.data.source.WisataDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WisataRepository @Inject constructor(
    private val wisataDataSource: WisataDataSource
) {

    fun getWisataByProvinceAndCity(provinceId: Int, cityId: Int): Flow<PagingData<Wisata>> {
        return wisataDataSource.getStreamWisataByProvinceAndCity(provinceId, cityId)
    }

    suspend fun getWisataDetail(wisataUuid: String): Flow<ApiResponse<Wisata>> {
        return wisataDataSource.getWisataById(wisataUuid)
    }
}