package com.andmar.data.images.entity

import com.andmar.data.images.network.PSImagesReponseDTO

data class ImagesResult(val maxHits: Int, val page: Int, val images: List<PSImage>) {


    companion object {
        fun fromDTO(dto: PSImagesReponseDTO, page:Int): ImagesResult {
            return ImagesResult(
                maxHits = dto.totalHits,
                page = page,
                images = dto.hits.map { hitDTO ->
                    PSImage(
                        id = hitDTO.id.toString(),
                        thumbUrl = hitDTO.previewURL,
                        username = hitDTO.user,
                        tags = hitDTO.tags.split(",")
                    )
                }
            )
        }
    }

}

