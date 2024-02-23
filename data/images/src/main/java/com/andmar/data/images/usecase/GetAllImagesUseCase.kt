package com.andmar.data.images.usecase

import com.andmar.data.images.ImagesRepository
import javax.inject.Inject

class GetAllImagesUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
){
     operator fun invoke() = imagesRepository.getAllImages()
}