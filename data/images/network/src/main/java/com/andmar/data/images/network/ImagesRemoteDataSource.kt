package com.andmar.data.images.network

import com.andmar.data.images.network.exceptions.ImageNotFoundException
import com.andmar.data.images.network.exceptions.RateLimitException
import com.andmar.data.images.network.exceptions.UnknownImageException
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
     * @throws ImageNotFoundException if the image is not found
     * @throws RateLimitException if the rate limit is exceeded
     * @throws UnknownImageException if the image is unknown
     */
    @Throws(ImageNotFoundException::class, RateLimitException::class, UnknownImageException::class)
    suspend fun getImages(query: String, page: Int, maxPageSize: Int): PSImagesResponseDTO

    /**
     * Returns the image with the given id
     * @param id the id of the image
     * @throws ImageNotFoundException if the image is not found
     * @throws RateLimitException if the rate limit is exceeded
     * @throws UnknownImageException if the image is unknown
     */
    @Throws(ImageNotFoundException::class, RateLimitException::class, UnknownImageException::class)
    suspend fun getImageWithId(id: Int): PSImagesResponseDTO
}