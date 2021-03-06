package com.ibnu.jelajahin.core.di

import com.ibnu.jelajahin.core.data.remote.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideEventService(retrofit: Retrofit): EventService {
        return retrofit.create(EventService::class.java)
    }

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun provideWisataService(retrofit: Retrofit): WisataService {
        return retrofit.create(WisataService::class.java)
    }

    @Provides
    fun provideRestaurantService(retrofit: Retrofit): RestaurantService {
        return retrofit.create(RestaurantService::class.java)
    }

    @Provides
    fun providePenginapanService(retrofit: Retrofit): PenginapanService {
        return retrofit.create(PenginapanService::class.java)
    }

    @Provides
    fun provideGemService(retrofit: Retrofit): DiscoveryService {
        return retrofit.create(DiscoveryService::class.java)
    }

}