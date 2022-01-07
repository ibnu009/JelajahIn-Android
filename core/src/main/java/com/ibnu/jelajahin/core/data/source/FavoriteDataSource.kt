package com.ibnu.jelajahin.core.data.source

import com.ibnu.jelajahin.core.data.local.entities.FavoriteEntity
import com.ibnu.jelajahin.core.data.local.room.FavoriteDao
import com.ibnu.jelajahin.core.data.local.room.query.TypeUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao
) {

    suspend fun getAllFavorites(type: String, savedBy: String): List<FavoriteEntity> {
        val query = TypeUtils.getFavoriteByType(type, savedBy)
        return favoriteDao.getAllFavorites(query)
    }

    suspend fun insertItemToFavorite(favoriteItem: FavoriteEntity) =
        favoriteDao.insertItemToFavorite(favoriteItem)

    suspend fun removeItemFromFavorite(uuid: String, savedBy: String) =
        favoriteDao.deleteItemFromFavorite(uuid, savedBy)

    suspend fun checkIsItemAlreadyFavorite(uuid: String, savedBy: String) =
        favoriteDao.checkIsItemAlreadyFavorite(uuid, savedBy)

}