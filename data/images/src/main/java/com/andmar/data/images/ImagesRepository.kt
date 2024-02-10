package com.andmar.data.images

import androidx.paging.PagingData
import com.andmar.data.images.entity.ImagesResult
import com.andmar.data.images.entity.PSImage
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    fun getImages(query: String, page: Int = 1): Flow<ImagesResult>

    fun getImagesWithPagingSource(query: String): Flow<PagingData<PSImage>>
}