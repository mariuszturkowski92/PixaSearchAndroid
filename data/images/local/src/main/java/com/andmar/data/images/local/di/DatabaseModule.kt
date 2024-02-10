package com.andmar.data.images.local.di

import android.content.Context
import androidx.room.Room
import com.andmar.data.images.local.ImageDBDao
import com.andmar.data.images.local.ImagesDatabase
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.local.ImagesLocalDataSourceImpl
import com.andmar.data.images.local.ImagesQueryDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    internal fun provideDataBase(@ApplicationContext context: Context): ImagesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ImagesDatabase::class.java,
            "images.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    internal fun provideImageQueryDao(database: ImagesDatabase): ImagesQueryDao = database.imagesQueryDao()

    @Provides
    internal fun provideImageDBDao(database: ImagesDatabase): ImageDBDao = database.imagesDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ImagesLocalDataSourceModule {
    @Singleton
    @Binds
    internal abstract fun bindImagesLocalDataSource(dataSource: ImagesLocalDataSourceImpl): ImagesLocalDataSource
}