package com.ibnu.jelajahin.ui.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.model.Gem
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.DiscoveryRepository
import com.ibnu.jelajahin.core.data.repository.WisataRepository
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

}