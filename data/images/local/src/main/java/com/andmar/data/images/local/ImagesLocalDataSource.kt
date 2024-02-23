package com.andmar.data.images.local

import androidx.paging.PagingSource
import com.andmar.data.images.local.entity.ImageWithQueryDB
import kotlinx.coroutines.flow.Flow

interface ImagesLocalDataSource {

    /**
     * Returns PagingSource for the images with the given query
     * @param query the query for which the images are to be returned
     */
    fun getImagesWithQueryPagingSource(query: String): PagingSource<Int, ImageWithQueryDB>

    /**
     * Returns all the images stored in local data source
     */
    fun getAllImagesFlow(): Flow<List<ImageWithQueryDB>>

    /**
     * Refreshes the images with the new images
     * @param data the new images to be added
     */
    suspend fun refreshImagesWithQuery(data: List<ImageWithQueryDB>)

    /**
     * Updates the existing query with the new images
     * @param data the new images to be added
     * @param page the page number of the new images
     */
    suspend fun updateExistingQueryWIthImages(data: List<ImageWithQueryDB>, page: Int)

    /**
     * Returns the count of images stored in local data source for the given query
     * @param query the query for which the count is to be returned
     */
    suspend fun countWith(query: String): Int

    /**
     * Returns the current page stored in local data source for the given query
     */
    suspend fun getCurrentPage(query: String): Int

    /**
     * Deletes the oldest images if the count exceeds the cache limit
     * cache limit is defined in [MAX_QUERIES_CACHED]
     */
    suspend fun deleteOldestIfCountExceedCacheLimit()

    suspend fun isImageExists(id: Int): Boolean
    fun getImageWithId(id: Int): Flow<ImageWithQueryDB>
    suspend fun updateSingleImage(imageWithQueryDB: ImageWithQueryDB)

    companion object {
        const val MAX_EMPTY_QUERIES_IMAGES_CACHED = 5
        const val MAX_QUERIES_CACHED = 5
        const val EMPTY_QUERY = ""
    }

}