package com.andmar.data.images.local.mock

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingSource
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.local.entity.ImageWithQueryDB
import kotlinx.coroutines.flow.Flow


@VisibleForTesting
class TestImagesLocalDataSource : ImagesLocalDataSource {
    override fun getImagesWithQueryPagingSource(query: String): PagingSource<Int, ImageWithQueryDB> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshImagesWithQuery(data: List<ImageWithQueryDB>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateExistingQueryWIthImages(data: List<ImageWithQueryDB>, page: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun countWith(query: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentPage(query: String): Int {
        TODO("Not yet implemented")
    }

    // create in memory room database


    override suspend fun deleteOldestIfCountExceedCacheLimit() {
        TODO("Not yet implemented")
    }

    override suspend fun isImageExists(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getImageWithId(id: Int): Flow<ImageWithQueryDB> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSingleImage(imageWithQueryDB: ImageWithQueryDB) {
        TODO("Not yet implemented")
    }


}