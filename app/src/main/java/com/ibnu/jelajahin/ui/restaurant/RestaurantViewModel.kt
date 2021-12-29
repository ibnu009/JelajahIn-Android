package com.ibnu.jelajahin.ui.restaurant

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.Restaurant
import com.ibnu.jelajahin.core.data.model.Review
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.RestaurantRepository
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
class RestaurantViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {

    var postState: PostStateHandler? = null

    fun getListRestaurant(provinceId: Int, searchQuery: String): Flow<PagingData<Restaurant>> {
        return repository.getRestaurant(provinceId, searchQuery).cachedIn(viewModelScope)
    }

    fun getRestaurantDetail(wisataUuid: String): LiveData<ApiResponse<Restaurant>> {
        val result = MutableLiveData<ApiResponse<Restaurant>>()
        viewModelScope.launch {
            repository.getRestaurantDetail(wisataUuid).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getRestaurantLocations(provinceId: Int): LiveData<ApiResponse<List<Restaurant>>> {
        val result = MutableLiveData<ApiResponse<List<Restaurant>>>()
        viewModelScope.launch {
            repository.getRestaurantLocations(provinceId).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getListReview(uuidRestaurant: String): LiveData<ApiResponse<List<Review>>> {
        val result = MutableLiveData<ApiResponse<List<Review>>>()
        viewModelScope.launch {
            repository.getReviewRestaurant(uuidRestaurant).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun uploadUlasan(
        context: Context, lifecycleOwner: LifecycleOwner,
        token: String, title: String, content: String, ratingService: Int, ratingFood: Int, ratingClean: Int, uuidRestaurant: String, path: String
    ) {
        MultipartUploadRequest(context, JelajahinConstValues.POST_RESTAURANT_ULASAN_URL)
            .setMethod("POST")
            .addHeader("token", token)
            .addParameter("title", title)
            .addParameter("content", content)
            .addParameter("rating_service", ratingService.toString())
            .addParameter("rating_food", ratingFood.toString())
            .addParameter("rating_clean", ratingClean.toString())
            .addParameter("uuidRestaurant", uuidRestaurant)
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