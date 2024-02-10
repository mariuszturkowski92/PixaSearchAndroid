package com.andmar.data.images.local

import androidx.paging.PagingSource
import com.andmar.data.images.local.entity.ImageDB
import com.andmar.data.images.local.entity.ImageQueryWithImages

interface ImagesLocalDataSource {
    suspend fun getImageQuery(query: String): ImageQueryWithImages?
    suspend fun insertOrUpdateImageQueryWithImages(imageQueryWithImages: ImageQueryWithImages)

    suspend fun deleteOldestIfCountExceedCacheLimit()
    suspend fun insertNewQuery(query: String, images: List<ImageDB>)
    suspend fun insertNewImagesFor(query: String, images: List<ImageDB>)
    fun getImagesPagingSource(query: String): PagingSource<Int, ImageDB>
}