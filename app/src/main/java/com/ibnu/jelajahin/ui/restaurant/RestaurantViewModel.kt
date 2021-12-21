package com.ibnu.jelajahin.ui.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {

    fun getListRestaurant(provinceId: Int, searchQuery: String): Flow<PagingData<Restaurant>> {
        return repository.getRestaurant(provinceId, searchQuery).cachedIn(viewModelScope)
    }


    fun getRestaurantDetail(wisataUuid: String): LiveData<ApiResponse<Restaurant>> {
        val result = MutableLiveData<ApiResponse<Restaurant>>()
        viewModelScope.launch {
            repository.getRestaurantDetail(wisataUuid).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getRestaurantLocations(provinceId: Int): LiveData<ApiResponse<List<Restaurant>>> {
        val result = MutableLiveData<ApiResponse<List<Restaurant>>>()
        viewModelScope.launch {
            repository.getRestaurantLocations(provinceId).collect {
                result.postValue(it)
            }
        }
        return result
    }
}