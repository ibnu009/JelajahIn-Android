package com.ibnu.jelajahin.core.data.repository

import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.source.EventDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventDataSource: EventDataSource
) {

    fun getEventByProvinceAndCity(provinceId: Int, cityId: Int, searchQuery: String): Flow<PagingData<Event>>{
        return eventDataSource.getStreamEventByProvinceAndCity(provinceId, cityId, searchQuery)
    }

    suspend fun getEventDetail(eventUuid: String): Flow<ApiResponse<Event>>{
        return eventDataSource.getEventById(eventUuid)
    }
}