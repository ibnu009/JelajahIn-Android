package com.ibnu.jelajahin.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.PointHistory
import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var currenHistory: Flow<PagingData<PointHistory>>? = null

    fun getUserPointHistory(): Flow<PagingData<PointHistory>> {
        val lastResult = currenHistory
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<PointHistory>> = userRepository.getUserPointHistory()
            .cachedIn(viewModelScope)
        currenHistory = newResult
        return newResult
    }

    fun getUserProfile(token: String): LiveData<ApiResponse<User>> {
        val result = MutableLiveData<ApiResponse<User>>()
        viewModelScope.launch {
            userRepository.getUserProfile(token).collect {
                result.postValue(it)
            }
        }
        return result
    }




}