package com.andmar.data.images

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.andmar.common.utils.IoDispatcher
import com.andmar.common.utils.error.Error
import com.andmar.common.utils.result.Result
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.local.ImagesLocalDataSource.Companion.EMPTY_QUERY
import com.andmar.data.images.network.ImagesRemoteDataSource
import com.andmar.data.images.network.exceptions.ImageNotFoundException
import com.andmar.data.images.network.exceptions.RateLimitException
import com.andmar.data.images.network.exceptions.UnknownImageException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val MAX_PAGE_SIZE = 5

@Singleton
internal class ImagesRepositoryImpl @Inject constructor(
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val searchPagingConfig: PagingConfig,
) : ImagesRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getImagesWithPagingSource(query: String): Flow<PagingData<PSImage>> {
        return Pager(
            config = searchPagingConfig,
            remoteMediator = ImagesRemoteMediator(
                query = query,
                imagesRemoteDataSource = imagesRemoteDataSource,
                imagesLocalDataSource = imagesLocalDataSource,
                ioDispatcher = ioDispatcher
            ),
            pagingSourceFactory = {
                imagesLocalDataSource.getImagesWithQueryPagingSource(query)
            }

        ).flow.map { pagingData ->
            pagingData.map { Mapper.mapFromImageWithQueryDBToPSImage(it) }
        }
    }

    override suspend fun getImageWithId(
        id: Int,
        forceFromRemote: Boolean,
    ): Flow<Result<PSImage, DataError>> {
        if (forceFromRemote || !imagesLocalDataSource.isImageExists(id)) {
            val newImageResponse = try {
                imagesRemoteDataSource.getImageWithId(id)
            } catch (e: ImageNotFoundException) {
                return flowOf(Result.Failure(DataError.ImageNotFound))
            } catch (e: RateLimitException) {
                return flowOf(Result.Failure(DataError.NoMoreRequest(e.waitingTime)))
            } catch (e: UnknownImageException) {
                return flowOf(Result.Failure(DataError.Unknown))
            }
            val imageWithQueryDB = Mapper.mapFromPSImagesResponseDTOToImagesWithQueryDB(
                EMPTY_QUERY,
                newImageResponse
            ).firstOrNull()
            if (imageWithQueryDB != null) {
                imagesLocalDataSource.updateSingleImage(imageWithQueryDB)
            } else {
                return flowOf(Result.Failure(DataError.ImageNotFound))
            }
        }
        return imagesLocalDataSource.getImageWithId(id)
            .map { Mapper.mapFromImageWithQueryDBToPSImage(it) }
            .map { Result.Success(it) }



    }
}

sealed class DataError : Error {
    data object ImageNotFound : DataError()
    data class NoMoreRequest(val waitingTime: Long) : DataError()

    data object StorageAccessError : DataError()

    data object Unknown : DataError()

}