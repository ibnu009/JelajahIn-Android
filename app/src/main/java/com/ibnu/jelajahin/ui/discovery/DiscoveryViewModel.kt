package com.ibnu.jelajahin.ui.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.model.Gem
import com.ibnu.jelajahin.core.data.model.Memorial
import com.ibnu.jelajahin.core.data.model.Typical
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.DiscoveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val discoveryRepository: DiscoveryRepository
) : ViewModel() {

    fun getAllGems(provinceId: Int, cityId: Int): LiveData<ApiResponse<List<Gem>>> {
        val result = MutableLiveData<ApiResponse<List<Gem>>>()
        viewModelScope.launch {
            discoveryRepository.getAllGems(provinceId, cityId).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getGemDetail(gemUuid: String): LiveData<ApiResponse<Gem>> {
        val result = MutableLiveData<ApiResponse<Gem>>()
        viewModelScope.launch {
            discoveryRepository.getGemDetail(gemUuid).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getAllTypical(provinceId: Int, cityId: Int): LiveData<ApiResponse<List<Typical>>> {
        val result = MutableLiveData<ApiResponse<List<Typical>>>()
        viewModelScope.launch {
            discoveryRepository.getAllTypical(provinceId, cityId).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getTypicalDetail(uuidTypical: String): LiveData<ApiResponse<Typical>> {
        val result = MutableLiveData<ApiResponse<Typical>>()
        viewModelScope.launch {
            discoveryRepository.getTypicalDetail(uuidTypical).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getAllMemorial(provinceId: Int, cityId: Int): LiveData<ApiResponse<List<Memorial>>> {
        val result = MutableLiveData<ApiResponse<List<Memorial>>>()
        viewModelScope.launch {
            discoveryRepository.getAllMemorials(provinceId, cityId).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getMemorialDetail(uuidMemorial: String): LiveData<ApiResponse<Memorial>> {
        val result = MutableLiveData<ApiResponse<Memorial>>()
        viewModelScope.launch {
            discoveryRepository.getMemorialDetail(uuidMemorial).collect {
                result.postValue(it)
            }
        }
        return result
    }

}