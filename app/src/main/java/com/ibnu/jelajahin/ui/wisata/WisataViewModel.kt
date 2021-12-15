package com.ibnu.jelajahin.ui.wisata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.WisataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WisataViewModel @Inject constructor(
    private val wisataRepository: WisataRepository,
) : ViewModel() {


    fun getListWisata(provinceId: Int, cityId: Int, searchQuery: String): Flow<PagingData<Wisata>> {
        return wisataRepository.getWisataByProvinceAndCity(provinceId, cityId, searchQuery)
            .cachedIn(viewModelScope)
    }

    fun getSearchWisata(provinceId: Int, cityId: Int, searchQuery: String): Flow<PagingData<Wisata>> {
        return wisataRepository.searchWisata(provinceId, cityId, searchQuery)
            .cachedIn(viewModelScope)
    }

    fun getWisataDetail(wisataUuid: String): LiveData<ApiResponse<Wisata>> {
        val result = MutableLiveData<ApiResponse<Wisata>>()
        viewModelScope.launch {
            wisataRepository.getWisataDetail(wisataUuid).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getWisataLocations(provinceId: Int, cityId: Int): LiveData<ApiResponse<List<Wisata>>> {
        val result = MutableLiveData<ApiResponse<List<Wisata>>>()
        viewModelScope.launch {
            wisataRepository.getWisataLocations(provinceId, cityId).collect {
                result.postValue(it)
            }
        }
        return result
    }
}