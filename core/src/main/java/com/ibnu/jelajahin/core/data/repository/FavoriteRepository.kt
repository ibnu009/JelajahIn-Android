package com.ibnu.jelajahin.core.data.repository

import com.ibnu.jelajahin.core.data.local.entities.FavoriteEntity
import com.ibnu.jelajahin.core.data.source.FavoriteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteDataSource: FavoriteDataSource
) {

    suspend fun getAllFavorites(type: String, savedBy: String): List<FavoriteEntity> =
        favoriteDataSource.getAllFavorites(type, savedBy)

    suspend fun insertItemToFavorite(favoriteItem: FavoriteEntity) =
        favoriteDataSource.insertItemToFavorite(favoriteItem)

    suspend fun removeItemFromFavorite(uuid: String, savedBy: String) =
        favoriteDataSource.removeItemFromFavorite(uuid, savedBy)

    suspend fun checkIsItemAlreadyFavorite(uuid: String, savedBy: String) : Boolean{
        val favoriteId = favoriteDataSource.checkIsItemAlreadyFavorite(uuid, savedBy)
        return favoriteId ?: -1 > -1
    }

}