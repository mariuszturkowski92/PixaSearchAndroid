package com.andmar.data.images.network

import com.andmar.data.images.network.model.PSImagesResponseDTO

interface ImagesRemoteDataSource {
    suspend fun getImages(query: String, page: Int): PSImagesResponseDTO

    companion object {
        const val BASE_URL = "https://pixabay.com/api/" //TODO move to gradle.properties
    }
}