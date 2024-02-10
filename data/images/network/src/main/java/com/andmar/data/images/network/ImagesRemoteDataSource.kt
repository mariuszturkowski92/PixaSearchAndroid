package com.andmar.data.images.network

import kotlinx.coroutines.flow.Flow

interface ImagesRemoteDataSource {
    suspend fun getImages(query: String, page: Int): PSImagesReponseDTO

    companion object {
        const val BASE_URL = "https://pixabay.com/api/" //TODO move to gradle.properties
    }
}