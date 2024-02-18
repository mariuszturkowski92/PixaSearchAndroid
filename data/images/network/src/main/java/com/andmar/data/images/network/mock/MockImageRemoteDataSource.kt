package com.andmar.data.images.network.mock

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

    override suspend fun getImageWithId(id: Int): PSImagesResponseDTO {
        return imagesResponse.first { it.hits.any { it.id == id } }
    }

    fun clearPosts() {
        imagesResponse.clear()
    }
}