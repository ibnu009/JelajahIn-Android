package com.ibnu.jelajahin.core.di

import com.ibnu.jelajahin.core.data.remote.network.EventService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideWisataService(retrofit: Retrofit): EventService {
        return retrofit.create(EventService::class.java)
    }


}