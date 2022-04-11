package com.ibnu.jelajahin.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ibnu.jelajahin.core.data.local.entities.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class JelajahinDatabase : RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao

}