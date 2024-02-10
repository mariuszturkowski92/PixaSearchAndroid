package com.andmar.data.images.local

import com.andmar.data.images.local.entity.ImageQueryDB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImagesLocalDataSourceImpl @Inject constructor(private val dao: ImagesDao) :
    ImagesLocalDataSource {
    override suspend fun getImageQuery(query: String): ImageQueryDB {
        return dao.findByQuery(query)
    }
}