package com.ibnu.jelajahin.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.jelajahin.core.data.local.entities.FavoriteEntity
import com.ibnu.jelajahin.core.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository) :
    ViewModel() {

    fun getBookmarkedItem(type: String, email: String): LiveData<List<FavoriteEntity>> {
        val result = MutableLiveData<List<FavoriteEntity>>()
        viewModelScope.launch {
            result.postValue(favoriteRepository.getAllFavorites(type, email))
        }
        return result
    }

    fun insertItemToFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.insertItemToFavorite(favoriteEntity)
        }
    }

    fun removeItemFromFavorite(uuid: String, savedBy: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.removeItemFromFavorite(uuid, savedBy)
        }
    }


}