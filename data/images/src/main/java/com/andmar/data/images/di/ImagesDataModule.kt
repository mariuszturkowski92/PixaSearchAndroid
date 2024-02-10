package com.andmar.data.images.di


import com.andmar.data.images.ImagesRepository
import com.andmar.data.images.ImagesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindImagesRepository(repository: ImagesRepositoryImpl): ImagesRepository
}
