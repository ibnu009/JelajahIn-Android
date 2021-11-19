package com.ibnu.jelajahin.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.network.EventService
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.DEFAULT_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventDataSource @Inject constructor(
private val eventService: EventService
)  {

    fun getStreamEventByProvinceAndCity(provinceId: Int, cityId: Int): Flow<PagingData<Event>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {EventPagingFactory(eventService, provinceId, cityId)}
        ).flow
    }

    suspend fun getEventById(eventUuid: String): Flow<ApiResponse<Event>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = eventService.getEventDetail(eventUuid)
                if (response.rowCount > 0){
                    emit(ApiResponse.Success(response.event))
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