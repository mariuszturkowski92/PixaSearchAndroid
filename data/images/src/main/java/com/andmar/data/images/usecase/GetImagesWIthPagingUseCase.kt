package com.andmar.data.images.usecase

import androidx.paging.PagingData
import com.andmar.data.images.ImagesRepository
import com.andmar.data.images.entity.PSImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImagesWithPagingUseCase @Inject constructor(private val repository: ImagesRepository) {
    operator fun invoke(query: String): Flow<PagingData<PSImage>> {
        return repository.getImagesWithPagingSource(query)
    }
}