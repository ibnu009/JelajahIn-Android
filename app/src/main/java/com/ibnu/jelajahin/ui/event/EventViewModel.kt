package com.ibnu.jelajahin.ui.event

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.request.PointBody
import com.ibnu.jelajahin.core.data.repository.EventRepository
import com.ibnu.jelajahin.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository) :
    ViewModel() {

    private var currentEvent: Flow<PagingData<Event>>? = null

    fun getEvents(provinceId: Int, cityId: Int, searchQuery: String): Flow<PagingData<Event>> {
        val lastResult = currentEvent
        if (lastResult != null){
            return lastResult
        }
        val newResult : Flow<PagingData<Event>> = eventRepository.getEventByProvinceAndCity(provinceId, cityId, searchQuery)
            .cachedIn(viewModelScope)
        currentEvent = newResult
        return newResult
    }

    fun addUserPoint(token: String, request: PointBody): LiveData<ApiResponse<String>> {
        val result = MutableLiveData<ApiResponse<String>>()
        viewModelScope.launch {
            userRepository.addUserPoint(token, request).collect {
                result.postValue(it)
            }
        }
        return result
    }
}