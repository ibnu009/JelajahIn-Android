package com.ibnu.jelajahin.ui.profile

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.PointHistory
import com.ibnu.jelajahin.core.data.model.User
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.UserRepository
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
import com.ibnu.jelajahin.core.utils.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private var currenHistory: Flow<PagingData<PointHistory>>? = null

    fun getUserPointHistory(token: String): Flow<PagingData<PointHistory>> {
        val lastResult = currenHistory
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<PointHistory>> = userRepository.getUserPointHistory(token)
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

    fun editUserProfile(
        context: Context, lifecycleOwner: LifecycleOwner,
        token: String, name: String, email: String, address: String?, path: String
    ) {
        MultipartUploadRequest(context, JelajahinConstValues.EDIT_PROFILE_URL)
            .setMethod("PUT")
            .addHeader("token", token)
            .addParameter("name", name)
            .addParameter("email", email)
            .addParameter("origin", address ?: "")
            .setMaxRetries(2)
            .addFileToUpload(path, "image", contentType = "image/*")
            .subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object :
                RequestObserverDelegate {
                override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                    Timber.d("Material Sudah $uploadInfo")
                }

                override fun onCompletedWhileNotObserving() {
                    Timber.d("Material loh weh")
                }

                override fun onError(
                    context: Context,
                    uploadInfo: UploadInfo,
                    exception: Throwable
                ) {
                    exception.stackTrace
                }

                override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                    Timber.d("Material lagi proses")
                }

                override fun onSuccess(
                    context: Context,
                    uploadInfo: UploadInfo,
                    serverResponse: ServerResponse
                ) {
                    Timber.d("Material done terupload $uploadInfo,\n ${serverResponse.bodyString}")
                }
            })
    }




}