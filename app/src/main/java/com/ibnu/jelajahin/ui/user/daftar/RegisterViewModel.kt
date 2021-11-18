package com.ibnu.jelajahin.ui.user.daftar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.request.RegisterBody
import com.ibnu.jelajahin.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun register(registerBody: RegisterBody): LiveData<ApiResponse<String>> {
        val result = MutableLiveData<ApiResponse<String>>()
        viewModelScope.launch {
            userRepository.register(registerBody).collect {
                result.postValue(it)
            }
        }
        return result
    }

}