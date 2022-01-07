package com.ibnu.jelajahin.core.di

import android.content.Context
import androidx.room.Room
import com.ibnu.jelajahin.core.data.local.room.FavoriteDao
import com.ibnu.jelajahin.core.data.local.room.JelajahinDatabase
import com.ibnu.jelajahin.core.utils.JelajahinConstValues.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): JelajahinDatabase {
        return Room.databaseBuilder(
            context,
            JelajahinDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(database: JelajahinDatabase): FavoriteDao = database.getFavoriteDao()

}