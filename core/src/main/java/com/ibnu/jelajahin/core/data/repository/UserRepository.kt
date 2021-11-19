package com.ibnu.jelajahin.core.data.repository

import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.request.LoginBody
import com.ibnu.jelajahin.core.data.remote.request.RegisterBody
import com.ibnu.jelajahin.core.data.source.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDataSource: UserDataSource) {

    suspend fun login(request: LoginBody): Flow<ApiResponse<String>> {
        return userDataSource.loginUser(request).flowOn(Dispatchers.IO)
    }

    suspend fun register(request: RegisterBody): Flow<ApiResponse<String>> {
        return userDataSource.registerUser(request).flowOn(Dispatchers.IO)
    }

    suspend fun getUserProfile(token: String): Flow<ApiResponse<User>> {
        return userDataSource.fetchUserProfile(token).flowOn(Dispatchers.IO)
    }
}