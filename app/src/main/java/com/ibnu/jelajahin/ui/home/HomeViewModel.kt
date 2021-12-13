package com.ibnu.jelajahin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.model.Ads
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
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

}