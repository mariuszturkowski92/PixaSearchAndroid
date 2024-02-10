package com.andmar.data.images

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.local.entity.ImageDB
import com.andmar.data.images.network.ImagesRemoteDataSource
import java.io.IOException
import kotlin.math.ceil

@OptIn(ExperimentalPagingApi::class)
class ImagesRemoteMediator(
    private val query: String,
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
) : RemoteMediator<Int, ImageDB>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageDB>,
    ): MediatorResult {
        return try {
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
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    ceil(state.pages.size.toDouble() / MAX_PAGE_SIZE.toDouble()).toInt() + 1
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            val response = imagesRemoteDataSource.getImages(query, page, MAX_PAGE_SIZE)


            if (loadType == LoadType.REFRESH) {
                imagesLocalDataSource.insertNewQuery(
                    query,
                    Mapper.mapFromPSImagesResponseDTOToImagesDB(response)
                )
            }

            // Insert new users into database, which invalidates the
            // current PagingData, allowing Paging to present the updates
            // in the DB.
            imagesLocalDataSource.insertNewImagesFor(
                query,
                Mapper.mapFromPSImagesResponseDTOToImagesDB(response)
            )


            MediatorResult.Success(
                endOfPaginationReached = response.hits.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } //TODO handle http exception
    }
}