package com.andmar.data.images

import com.andmar.data.images.entity.ImagesResult
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    fun getImages(query: String, page: Int = 1): Flow<ImagesResult>

}