package com.example.yumfinder.di

import android.content.Context
import com.example.yumfinder.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.yumfinder.data.RestaurantDAO
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideRestaurantDAO(appDatabase: AppDatabase): RestaurantDAO {
        return appDatabase.restaurantDAO()
    }

    @Provides
    @Singleton
    fun provideRestaurantDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }
}