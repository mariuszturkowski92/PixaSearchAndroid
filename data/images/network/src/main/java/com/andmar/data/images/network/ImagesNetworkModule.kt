package com.andmar.data.images.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImagesNetworkModule {

    @Singleton
    @Provides
    fun provideNetworkDataSource( client: HttpClient): ImagesRemoteDataSource{
        return ImagesRemoteDataSourceImpl(apiKey = BuildConfig.PIXABAY_API_KEY ,client )
    }

}