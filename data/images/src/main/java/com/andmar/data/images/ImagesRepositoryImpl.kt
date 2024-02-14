package com.andmar.data.images

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.andmar.common.utils.IoDispatcher
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.network.ImagesRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val MAX_PAGE_SIZE = 20

@Singleton
internal class ImagesRepositoryImpl @Inject constructor(
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
) : ImagesRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getImagesWithPagingSource(query: String): Flow<PagingData<PSImage>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_PAGE_SIZE, prefetchDistance = 1),
            remoteMediator = ImagesRemoteMediator(
                query = query,
                imagesRemoteDataSource = imagesRemoteDataSource,
                imagesLocalDataSource = imagesLocalDataSource,
                ioDispatcher = ioDispatcher
            ),
            pagingSourceFactory = { imagesLocalDataSource.getImagesWithQueryPagingSource(query) }

        ).flow.map { pagingData ->
            pagingData.map { Mapper.mapFromImageWithQueryDBToPSImage(it) }
        }
    }
}