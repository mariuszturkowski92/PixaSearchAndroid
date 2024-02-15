package com.andmar.data.images.network

import com.andmar.data.images.network.model.PSImagesResponseDTO

interface ImagesRemoteDataSource {

    companion object {
        const val BASE_URL = "https://pixabay.com/api/"
    }

    /**
     * Returns the images for the given query
     * @param query the query for which the images are to be returned
     * @param page the page number of the images
     * @param maxPageSize the maximum page size
     */
    suspend fun getImages(query: String, page: Int, maxPageSize: Int): PSImagesResponseDTO
    suspend fun getImageWithId(id: Int): PSImagesResponseDTO
}