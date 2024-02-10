package com.andmar.data.images

import com.andmar.data.images.entity.ImagesResult
import com.andmar.data.images.network.ImagesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImagesRepositoryImpl @Inject constructor(
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
) : ImagesRepository {
    override fun getImages(query: String, page: Int): Flow<ImagesResult> {
        return flow {
            val response = imagesRemoteDataSource.getImages(query, page)
            emit(ImagesResult.fromDTO(response, page))
        }
    }
}