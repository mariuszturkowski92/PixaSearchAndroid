package com.andmar.data.images

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.local.entity.ImageWithQueryDB
import com.andmar.data.images.network.ImagesRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ImagesRemoteMediator(
    private val query: String,
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : RemoteMediator<Int, ImageWithQueryDB>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageWithQueryDB>,
    ): MediatorResult {
        return try {
            withContext(ioDispatcher) {
                val page = when (loadType) {
                    LoadType.REFRESH -> 1
                    LoadType.PREPEND ->
                        return@withContext MediatorResult.Success(endOfPaginationReached = true)

                    LoadType.APPEND -> {
                        val pageFromLDS =
                            imagesLocalDataSource.getCurrentPage(query)
                        pageFromLDS + 1
                    }
                }
                val response = imagesRemoteDataSource.getImages(query, page, MAX_PAGE_SIZE)
                if (response.hits.isEmpty()) {
                    return@withContext MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                if (loadType == LoadType.REFRESH) {
                    imagesLocalDataSource.refreshImagesWithQuery(
                        Mapper.mapFromPSImagesResponseDTOToImagesWithQueryDB(query, response)
                    )
                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                imagesLocalDataSource.updateExistingQueryWIthImages(
                    Mapper.mapFromPSImagesResponseDTOToImagesWithQueryDB(query, response), page
                )

                try {
                    imagesLocalDataSource.deleteOldestIfCountExceedCacheLimit()
                } catch (e: Exception) { // do not propagate this exception down the call stack, log it to be fixed in future.s
                    Timber.e(e)
                }

                MediatorResult.Success(
                    endOfPaginationReached = response.hits.size < MAX_PAGE_SIZE
                )
            }
        } catch (e: IOException) {
            Timber.e(e, "load: IOException")
            MediatorResult.Error(e)
        } catch (e: IllegalStateException) {
            Timber.e(e, "load: IllegalStateException")
            MediatorResult.Error(e)
        }
    }
}