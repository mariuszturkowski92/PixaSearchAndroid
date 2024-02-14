package com.andmar.data.images.local.di

import android.content.Context
import com.andmar.data.images.local.ImageWithQueryDao
import com.andmar.data.images.local.ImagesDatabase
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.local.ImagesLocalDataSourceImpl
import com.andmar.data.images.local.QueryDao
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
        return ImagesDatabase.create(context)
    }

    @Provides
    internal fun provideImageWithQueryDao(database: ImagesDatabase): ImageWithQueryDao =
        database.imageWithQueryDao()

    @Provides
    internal fun provideQueryDao(database: ImagesDatabase): QueryDao = database.queryDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ImagesLocalDataSourceModule {
    @Singleton
    @Binds
    internal abstract fun bindImagesLocalDataSource(dataSource: ImagesLocalDataSourceImpl): ImagesLocalDataSource
}