package com.andmar.data.images.usecase

import com.andmar.data.images.ImagesRepository
import com.andmar.data.images.entity.PSImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageWithIdUseCase @Inject constructor(private val repository: ImagesRepository) {
    suspend operator fun invoke(id: Int, forceReload: Boolean): Flow<PSImage> =
        repository.getImageWithId(id, forceReload)
}