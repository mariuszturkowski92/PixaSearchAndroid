package com.andmar.data.images.usecase

import com.andmar.common.utils.error.Error
import com.andmar.common.utils.result.Result
import com.andmar.common.utils.result.mapError
import com.andmar.data.images.DataError
import com.andmar.data.images.ImagesRepository
import com.andmar.data.images.entity.PSImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImageWithIdUseCase @Inject constructor(private val repository: ImagesRepository) {
    suspend operator fun invoke(
        id: Int,
        forceReload: Boolean
    ): Flow<Result<PSImage, GetImageError>> {
        return repository.getImageWithId(id, forceReload).map { result ->
            result.mapError { error ->
                when (error) {
                    is DataError.ImageNotFound -> GetImageError.ImageNotFound
                    is DataError.NoMoreRequest -> GetImageError.NoMoreRequest(error.waitingTime)
                    DataError.StorageAccessError -> GetImageError.Unknown
                    DataError.Unknown -> GetImageError.Unknown
                }

            }
        }
    }

}

sealed class GetImageError : Error {
    data object ImageNotFound : GetImageError()
    data class NoMoreRequest(val waitingTime: Long) : GetImageError()
    data object Unknown : GetImageError()
}