package com.andmar.data.images

import com.andmar.data.images.entity.ImageData
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.local.entity.ImageWithQueryDB
import com.andmar.data.images.network.model.PSImagesResponseDTO

object Mapper {

    fun mapFromPSImagesResponseDTOToImagesWithQueryDB(
        query: String,
        response: PSImagesResponseDTO,
    ): List<ImageWithQueryDB> {
        return response.hits.map { mapFromPSImageDTOToImageWithQueryDB(query, it) }
    }

    private fun mapFromPSImageDTOToImageWithQueryDB(
        query: String,
        it: PSImagesResponseDTO.HitDTO,
    ): ImageWithQueryDB {
        return ImageWithQueryDB(
            query = query,
            imageId = it.id,
            previewURL = it.previewURL,
            previewWidth = it.previewWidth,
            previewHeight = it.previewHeight,
            largeImageURL = it.largeImageURL,
            largeImageWidth = it.imageWidth,
            largeImageHeight = it.imageHeight,
            user = it.user,
            userImageURL = it.userImageURL,
            likes = it.likes,
            comments = it.comments,
            tags = it.tags,
            userId = it.userId,
            downloads = it.downloads
        )
    }

    fun mapFromImageWithQueryDBToPSImage(imageWithQueryDB: ImageWithQueryDB): PSImage {
        return PSImage(
            id = imageWithQueryDB.id!!,
            pixaId = imageWithQueryDB.imageId,
            thumbSource = ImageData(
                imageWithQueryDB.previewURL,
                imageWithQueryDB.previewHeight,
                imageWithQueryDB.previewWidth
            ),
            username = imageWithQueryDB.user,
            tags = imageWithQueryDB.tags.split(","),
            likes = imageWithQueryDB.likes,
            comments = imageWithQueryDB.comments,
            downloads = imageWithQueryDB.downloads,
            largeImage = ImageData(
                imageWithQueryDB.largeImageURL,
                imageWithQueryDB.largeImageHeight,
                imageWithQueryDB.largeImageWidth
            )
        )
    }
}