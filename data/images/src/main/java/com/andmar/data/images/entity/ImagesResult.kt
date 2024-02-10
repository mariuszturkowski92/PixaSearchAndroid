package com.andmar.data.images.entity

import com.andmar.data.images.network.model.PSImagesResponseDTO

data class ImagesResult(val maxHits: Int, val page: Int, val images: List<PSImage>) {


    companion object {
        fun fromDTO(dto: PSImagesResponseDTO, page: Int): ImagesResult {
            return ImagesResult(
                maxHits = dto.totalHits,
                page = page,
                images = dto.hits.map { hitDTO ->
                    PSImage(
                        id = hitDTO.id.toString(),
                        thumbSource = ImageData(hitDTO.previewURL, hitDTO.previewHeight, hitDTO.previewWidth),
                        username = hitDTO.user,
                        tags = hitDTO.tags.split(",")
                    )
                }
            )
        }
    }

}

