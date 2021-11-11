package com.ibnu.jelajahin.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    private var currentEvent: Flow<PagingData<Event>>? = null

    fun getEvents(provinceId: Int, cityId: Int): Flow<PagingData<Event>> {
        val lastResult = currentEvent
        if (lastResult != null){
            return lastResult
        }
        val newResult : Flow<PagingData<Event>> = eventRepository.getEventByProvinceAndCity(provinceId, cityId)
            .cachedIn(viewModelScope)
        currentEvent = newResult
        return newResult
    }

}