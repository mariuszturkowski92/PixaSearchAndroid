package com.andmar.data.images.usecase

import com.andmar.data.images.ImagesRepository
import javax.inject.Inject


class GetImagesUseCase @Inject constructor(private val imagesRepository: ImagesRepository) {

    operator fun invoke(query: String, page: Int) = imagesRepository.getImages(query, page)
}