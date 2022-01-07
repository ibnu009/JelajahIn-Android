package com.ibnu.jelajahin.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val Id: Int = 0,
    val uuid: String,
    @ColumnInfo(name = "favorite_type")
    val favoriteType: String,
    val name: String,
    val address: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "rating_avg")
    val ratingAvg: Double,
    @ColumnInfo(name = "rating_count")
    val ratingCount: Int,
    @ColumnInfo(name = "saved_by")
    val savedBy: String
)
