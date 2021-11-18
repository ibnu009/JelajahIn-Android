package com.ibnu.jelajahin.ui.user.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.model.Event
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.request.LoginBody
import com.ibnu.jelajahin.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun login(loginBody: LoginBody): LiveData<ApiResponse<String>> {
        val result = MutableLiveData<ApiResponse<String>>()
        viewModelScope.launch {
            userRepository.login(loginBody).collect {
                result.postValue(it)
            }
        }
        return result
    }

}