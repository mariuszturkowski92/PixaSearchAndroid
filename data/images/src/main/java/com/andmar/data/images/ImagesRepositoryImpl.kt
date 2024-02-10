package com.andmar.data.images

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.andmar.data.images.entity.ImagesResult
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.network.ImagesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val MAX_PAGE_SIZE = 20

@Singleton
internal class ImagesRepositoryImpl @Inject constructor(
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
) : ImagesRepository {
    override fun getImages(query: String, page: Int): Flow<ImagesResult> {
        return flow {
            val response = imagesRemoteDataSource.getImages(query, page, MAX_PAGE_SIZE)
            emit(ImagesResult.fromDTO(response, page))
            imagesLocalDataSource.insertOrUpdateImageQueryWithImages(
                Mapper.mapFromPSImagesResponseDTOToImageQueryWithImages(
                    response,
                    query,
                    page
                )
            )
            imagesLocalDataSource.deleteOldestIfCountExceedCacheLimit()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getImagesWithPagingSource(query: String): Flow<PagingData<PSImage>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_PAGE_SIZE, prefetchDistance = 1),
            remoteMediator = ImagesRemoteMediator(
                query = query,
                imagesRemoteDataSource = imagesRemoteDataSource,
                imagesLocalDataSource = imagesLocalDataSource
            ),
            pagingSourceFactory = { imagesLocalDataSource.getImagesPagingSource(query) }

        ).flow.map { pagingData ->

            pagingData.map { Mapper.mapFromImageDBToPSImage(it) }
        }
    }
}