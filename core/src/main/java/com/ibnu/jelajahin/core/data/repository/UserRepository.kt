package com.ibnu.jelajahin.core.data.repository

import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.PointHistory
import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.request.LoginBody
import com.ibnu.jelajahin.core.data.remote.request.RegisterBody
import com.ibnu.jelajahin.core.data.source.UserDataSource
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val sharedPreferenceManager: SharedPreferenceManager
) {

    suspend fun login(request: LoginBody): Flow<ApiResponse<String>> {
        return userDataSource.loginUser(request).flowOn(Dispatchers.IO)
    }

    suspend fun register(request: RegisterBody): Flow<ApiResponse<String>> {
        return userDataSource.registerUser(request).flowOn(Dispatchers.IO)
    }

    suspend fun getUserProfile(token: String): Flow<ApiResponse<User>> {
        return userDataSource.fetchUserProfile(token).flowOn(Dispatchers.IO)
    }

    fun getUserPointHistory(): Flow<PagingData<PointHistory>> {
        return userDataSource.fetchListPointHistory(sharedPreferenceManager.getToken ?: "")
    }
}