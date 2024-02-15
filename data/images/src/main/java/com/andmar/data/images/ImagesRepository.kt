package com.andmar.data.images

import androidx.paging.PagingData
import com.andmar.data.images.entity.PSImage
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    fun getImagesWithPagingSource(query: String): Flow<PagingData<PSImage>>
    suspend fun getImageWithId(id: Int, forceFromRemote: Boolean = false): Flow<PSImage>
}