package com.andmar.data.images.local

import androidx.paging.PagingSource
import com.andmar.data.images.local.entity.ImageWithQueryDB

interface ImagesLocalDataSource {

    // methods for ImageWithQueryDB
    fun getImagesWithQueryPagingSource(query: String): PagingSource<Int, ImageWithQueryDB>

    suspend fun refreshImagesWithQuery(data: List<ImageWithQueryDB>)

    suspend fun updateExistingQueryWIthImages(data: List<ImageWithQueryDB>, page: Int)

    suspend fun countWith(query: String): Int

    suspend fun getCurrentPage(query: String): Int

    suspend fun deleteOldestIfCountExceedCacheLimit()

}