package com.andmar.data.images.usecase

import com.andmar.data.images.ImagesRepository

class GetImageWithIdUseCase(private val repository: ImagesRepository) {
    suspend operator fun invoke(id: Int) = repository.getImageWithId(id)
}