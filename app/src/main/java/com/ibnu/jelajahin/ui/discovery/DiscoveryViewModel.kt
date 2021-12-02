package com.ibnu.jelajahin.ui.discovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.WisataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val wisataRepository: WisataRepository
) : ViewModel() {

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