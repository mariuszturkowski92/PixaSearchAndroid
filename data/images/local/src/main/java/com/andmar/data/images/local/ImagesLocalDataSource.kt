package com.andmar.data.images.local

import com.andmar.data.images.local.entity.ImageQueryDB

interface ImagesLocalDataSource {
    suspend fun getImageQuery(query: String): ImageQueryDB
}