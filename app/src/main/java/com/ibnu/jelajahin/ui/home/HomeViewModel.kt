package com.ibnu.jelajahin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.model.*
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.PenginapanRepository
import com.ibnu.jelajahin.core.data.repository.RestaurantRepository
import com.ibnu.jelajahin.core.data.repository.UserRepository
import com.ibnu.jelajahin.core.data.repository.WisataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val wisataRepository: WisataRepository,
    private val penginapanRepository: PenginapanRepository,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    fun getAds(provinceId: Int): LiveData<ApiResponse<List<Ads>>> {
        val result = MutableLiveData<ApiResponse<List<Ads>>>()
        viewModelScope.launch {
            userRepository.getAds(provinceId).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getWisataRecommendation(): LiveData<ApiResponse<List<Wisata>>> {
        val result = MutableLiveData<ApiResponse<List<Wisata>>>()
        viewModelScope.launch {
            wisataRepository.getWisataRecommendation().collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getPenginapanRecommendation(): LiveData<ApiResponse<List<Penginapan>>> {
        val result = MutableLiveData<ApiResponse<List<Penginapan>>>()
        viewModelScope.launch {
            penginapanRepository.getPenginapanRecommendation().collect {
                result.postValue(it)
            }
        }
        return result
    }


    fun getRestaurantRecommendation(): LiveData<ApiResponse<List<Restaurant>>> {
        val result = MutableLiveData<ApiResponse<List<Restaurant>>>()
        viewModelScope.launch {
            restaurantRepository.getRestaurantRecommendation().collect {
                result.postValue(it)
            }
        }
        return result
    }


}