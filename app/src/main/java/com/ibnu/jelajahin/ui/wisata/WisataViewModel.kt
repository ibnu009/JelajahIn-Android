package com.ibnu.jelajahin.ui.wisata

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ibnu.jelajahin.core.data.model.Wisata
import com.ibnu.jelajahin.core.data.remote.network.ApiResponse
import com.ibnu.jelajahin.core.data.repository.WisataRepository
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
class WisataViewModel @Inject constructor(
    private val wisataRepository: WisataRepository,
) : ViewModel() {

    var postState: PostStateHandler? = null

    fun getListWisata(provinceId: Int, cityId: Int, searchQuery: String): Flow<PagingData<Wisata>> {
        return wisataRepository.getWisataByProvinceAndCity(provinceId, cityId, searchQuery)
            .cachedIn(viewModelScope)
    }

    fun getSearchWisata(provinceId: Int, cityId: Int, searchQuery: String): Flow<PagingData<Wisata>> {
        return wisataRepository.searchWisata(provinceId, cityId, searchQuery)
            .cachedIn(viewModelScope)
    }

    fun getWisataDetail(wisataUuid: String): LiveData<ApiResponse<Wisata>> {
        val result = MutableLiveData<ApiResponse<Wisata>>()
        viewModelScope.launch {
            wisataRepository.getWisataDetail(wisataUuid).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getWisataLocations(provinceId: Int, cityId: Int): LiveData<ApiResponse<List<Wisata>>> {
        val result = MutableLiveData<ApiResponse<List<Wisata>>>()
        viewModelScope.launch {
            wisataRepository.getWisataLocations(provinceId, cityId).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun uploadUlasan(
        context: Context, lifecycleOwner: LifecycleOwner,
        token: String, title: String, content: String, rating: Int, uuidWisata: String, path: String
    ) {
        MultipartUploadRequest(context, JelajahinConstValues.POST_WISATA_ULASAN_URL)
            .setMethod("POST")
            .addHeader("token", token)
            .addParameter("title", title)
            .addParameter("content", content)
            .addParameter("rating", rating.toString())
            .addParameter("uuidWisata", uuidWisata)
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