package com.example.yumfinder.di

import android.content.Context
import com.example.yumfinder.data.location.LocationDataSource
import com.example.yumfinder.data.location.LocationDataSourceImpl
import com.example.yumfinder.data.location.LocationRepository
import com.example.yumfinder.data.location.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton // Specify the scope if needed
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    companion object { // Add a companion object
        @Provides
        @Singleton // Specify the scope if needed
        fun provideLocationDataSource(
            @ApplicationContext context: Context
        ): LocationDataSource {
            return LocationDataSourceImpl(context)
        }
    }
}