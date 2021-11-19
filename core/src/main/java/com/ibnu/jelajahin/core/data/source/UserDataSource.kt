package com.ibnu.jelajahin.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ibnu.jelajahin.core.data.model.PointHistory
import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.remote.network.UserService
import com.ibnu.jelajahin.core.data.remote.request.HistoryPointBody
import com.ibnu.jelajahin.core.data.remote.request.LoginBody
import com.ibnu.jelajahin.core.data.remote.request.RegisterBody
import com.ibnu.jelajahin.core.data.remote.response.UserResponse
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.DEFAULT_PAGE_SIZE
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

    suspend fun fetchUserProfile(token: String): Flow<ApiResponse<User>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = userService.getUserProfile(token)
                if (response.status == HttpURLConnection.HTTP_OK){
                    emit(ApiResponse.Success(response.user))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    fun fetchListPointHistory(token: String): Flow<PagingData<PointHistory>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {HistoryPointPagingFactory(userService, token)}
        ).flow
    }

}