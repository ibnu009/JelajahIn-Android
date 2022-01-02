package com.ibnu.jelajahin.ui.penginapan

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.Penginapan
import com.ibnu.jelajahin.core.data.model.Review
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.PenginapanRepository
import com.ibnu.jelajahin.core.ui.state.PostStateHandler
import com.ibnu.jelajahin.core.utils.JelajahinConstValues
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
class PenginapanViewModel @Inject constructor(private val repository: PenginapanRepository) :
    ViewModel() {

    var postState: PostStateHandler? = null

    fun getListPenginapan(provinceId: Int, searchQuery: String): Flow<PagingData<Penginapan>> {
        return repository.getPenginapan(provinceId, searchQuery).cachedIn(viewModelScope)
    }

    fun getPenginapanDetail(wisataUuid: String): LiveData<ApiResponse<Penginapan>> {
        val result = MutableLiveData<ApiResponse<Penginapan>>()
        viewModelScope.launch {
            repository.getPenginapanDetail(wisataUuid).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getPenginapanLocations(provinceId: Int): LiveData<ApiResponse<List<Penginapan>>> {
        val result = MutableLiveData<ApiResponse<List<Penginapan>>>()
        viewModelScope.launch {
            repository.getPenginapanLocations(provinceId).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getListReview(uuidPenginapan: String): LiveData<ApiResponse<List<Review>>> {
        val result = MutableLiveData<ApiResponse<List<Review>>>()
        viewModelScope.launch {
            repository.getReviewPenginapan(uuidPenginapan).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun uploadUlasan(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        token: String,
        title: String,
        content: String,
        ratingService: Int,
        ratingFriendly: Int,
        ratingClean: Int,
        uuidPenginapan: String,
        path: String
    ) {
        MultipartUploadRequest(context, JelajahinConstValues.POST_PENGINAPAN_ULASAN_URL)
            .setMethod("POST")
            .addHeader("token", token)
            .addParameter("title", title)
            .addParameter("content", content)
            .addParameter("rating_service", ratingService.toString())
            .addParameter("rating_friendly", ratingFriendly.toString())
            .addParameter("rating_clean", ratingClean.toString())
            .addParameter("uuidPenginapan", uuidPenginapan)
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
                    postState?.onFailure("Gagal posting ulasan!")
                }

                override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                    Timber.d("Material lagi proses")
                    postState?.onInitiating()
                }

                override fun onSuccess(
                    context: Context,
                    uploadInfo: UploadInfo,
                    serverResponse: ServerResponse
                ) {
                    Timber.d("Material done terupload $uploadInfo,\n ${serverResponse.bodyString}")
                    postState?.onSuccess("Berhasil mengirim ulasan!")
                }
            })
    }
}