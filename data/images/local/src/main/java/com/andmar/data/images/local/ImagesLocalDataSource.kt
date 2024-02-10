package com.andmar.data.images.local

import com.andmar.data.images.local.entity.ImageQueryDB
import com.andmar.data.images.local.entity.ImageQueryWithImages

interface ImagesLocalDataSource {
    suspend fun getImageQuery(query: String): ImageQueryWithImages?
    suspend fun insertOrUpdateImageQueryWithImages(imageQueryWithImages: ImageQueryWithImages)

    suspend fun deleteOldestIfCountExceedCacheLimit()
}