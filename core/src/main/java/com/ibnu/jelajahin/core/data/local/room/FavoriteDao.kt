package com.ibnu.jelajahin.core.data.local.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.ibnu.jelajahin.core.data.local.entities.FavoriteEntity

@Dao
interface FavoriteDao {

    @RawQuery(observedEntities = [FavoriteEntity::class])
    suspend fun getAllFavorites(query: SupportSQLiteQuery): List<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemToFavorite(favoriteData: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE uuid =:uuid AND saved_by = :savedBy")
    suspend fun deleteItemFromFavorite(uuid: String, savedBy: String)

    @Query("SELECT id FROM favorite WHERE uuid =:uuid AND saved_by = :savedBy")
    suspend fun checkIsItemAlreadyFavorite(uuid: String, savedBy: String): Int?

}