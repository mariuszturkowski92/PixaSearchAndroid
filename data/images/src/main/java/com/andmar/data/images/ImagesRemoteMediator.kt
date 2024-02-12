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
                // The network load method takes an optional after=<user.id>
                // parameter. For every page after the first, pass the last user
                // ID to let it continue from where it left off. For REFRESH,
                // pass null to load the first page.
                val page = when (loadType) {
                    LoadType.REFRESH -> 1
                    // In this example, you never need to prepend, since REFRESH
                    // will always load the first page in the list. Immediately
                    // return, reporting end of pagination.
                    LoadType.PREPEND ->
                        return@withContext MediatorResult.Success(endOfPaginationReached = true)

                    LoadType.APPEND -> {
                        val pageFromLDS =
                            imagesLocalDataSource.getCurrentPage(query)
                        Timber.d("load: pageFromLDS: $pageFromLDS")
                        pageFromLDS + 1
                    }
                }
                Timber.d("load: page: $page loadType: $loadType query: $query")

                // Suspending network load via Retrofit. This doesn't need to be
                // wrapped in a withContext(Dispatcher.IO) { ... } block since
                // Retrofit's Coroutine CallAdapter dispatches on a worker
                // thread.
                val response = imagesRemoteDataSource.getImages(query, page, MAX_PAGE_SIZE)
                Timber.d("load: response: ${response.hits.map { it.id }}")
                if (response.hits.isEmpty()) {
                    return@withContext MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                if (loadType == LoadType.REFRESH) {
                    imagesLocalDataSource.refreshImagesWithQuery(
                        Mapper.mapFromPSImagesResponseDTOToImagesWithQeryDB(query, response)
                    )
                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                Timber.d("images count before insertion: ${imagesLocalDataSource.countWith(query)} response count: ${response.hits.size}")
                imagesLocalDataSource.updateExistingQueryWIthImages(
                    Mapper.mapFromPSImagesResponseDTOToImagesWithQeryDB(query, response), page
                )
                Timber.d("images count after insertion: ${imagesLocalDataSource.countWith(query)}")


                MediatorResult.Success(
                    endOfPaginationReached = response.hits.isEmpty()
                )
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } //TODO handle http exception
    }
}