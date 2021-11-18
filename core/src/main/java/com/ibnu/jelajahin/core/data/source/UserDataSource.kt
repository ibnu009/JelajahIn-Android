package com.ibnu.jelajahin.core.data.source

import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.network.UserService
import com.ibnu.jelajahin.core.data.remote.request.LoginBody
import com.ibnu.jelajahin.core.data.remote.request.RegisterBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttp
import retrofit2.http.HTTP
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(private val userService: UserService){

    suspend fun loginUser(request: LoginBody): Flow<ApiResponse<String>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = userService.loginUser(request)
                if (response.status == HttpURLConnection.HTTP_OK){
                    emit(ApiResponse.Success(response.token))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun registerUser(request: RegisterBody): Flow<ApiResponse<String>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = userService.registerUser(request)
                if (response.status == HttpURLConnection.HTTP_OK){
                    emit(ApiResponse.Success(response.token))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

}