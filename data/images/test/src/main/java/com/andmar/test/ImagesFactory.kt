package com.andmar.test

import com.andmar.data.images.network.model.PSImagesResponseDTO

class ImagesFactory {
    fun createImageRemoteResponse(size: Int): PSImagesResponseDTO {
        val hits = (0 until size).map {
            PSImagesResponseDTO.HitDTO(
                collections = it,
                comments = it,
                downloads = it,
                id = it,
                imageHeight = it,
                imageSize = it,
                imageWidth = it,
                largeImageURL = "largeImageURL$it",
                likes = it,
                pageURL = "pageURL$it",
                previewHeight = it,
                previewURL = "previewURL$it",
                previewWidth = it,
                tags = "tags$it",
                type = "type$it",
                user = "user$it",
                userId = it,
                userImageURL = "userImageURL$it",
                views = it,
                webformatHeight = it,
                webformatURL = "webformatURL$it",
                webformatWidth = it
            )
        }
        return PSImagesResponseDTO(hits, hits.size, hits.size)
    }
}