package com.ibnu.jelajahin.ui.event.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    fun getEventDetail(eventUuid: String): LiveData<ApiResponse<Event>> {
        val result = MutableLiveData<ApiResponse<Event>>()
        viewModelScope.launch {
            eventRepository.getEventDetail(eventUuid).collect {
                result.postValue(it)
            }
        }
        return result
    }

}