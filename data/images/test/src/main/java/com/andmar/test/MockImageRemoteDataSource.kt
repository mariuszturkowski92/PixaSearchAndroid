package com.andmar.test

import com.andmar.data.images.network.ImagesRemoteDataSource
import com.andmar.data.images.network.model.PSImagesResponseDTO

class MockImageRemoteDataSource : ImagesRemoteDataSource {
    var failureMsg: String? = null

    val imagesResponse: MutableList<PSImagesResponseDTO> = mutableListOf()
    override suspend fun getImages(query: String, page: Int, maxPageSize: Int): PSImagesResponseDTO {
        return if (failureMsg != null) {
            throw Exception(failureMsg)
        } else {
            imagesResponse[page - 1]
        }
    }

    fun clearPosts() {
        imagesResponse.clear()
    }
}