package com.andmar.data.images.network

import com.andmar.data.images.network.model.PSImagesResponseDTO

interface ImagesRemoteDataSource {

    companion object {
        const val BASE_URL = "https://pixabay.com/api/" //TODO move to gradle.properties
    }

    suspend fun getImages(query: String, page: Int, maxPageSize: Int): PSImagesResponseDTO
}