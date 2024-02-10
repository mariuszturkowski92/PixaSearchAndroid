package com.andmar.data.images

import com.andmar.data.images.entity.ImageData
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.local.entity.ImageDB
import com.andmar.data.images.local.entity.ImageQueryDB
import com.andmar.data.images.local.entity.ImageQueryWithImages
import com.andmar.data.images.network.model.PSImagesResponseDTO

object Mapper {
    fun mapFromPSImagesResponseDTOToImageQueryWithImages(
        psImagesResponseDTO: PSImagesResponseDTO,
        query: String,
        page: Int,
    ): ImageQueryWithImages {
        val imageQueryDB =
            ImageQueryDB(query = query, fetchedPages = page, totalResults = psImagesResponseDTO.totalHits)

        val images = psImagesResponseDTO.hits.map { mapFromPSImageDTOToImageDB(it) }
        return ImageQueryWithImages(imageQueryDB, images)
    }

    fun mapFromPSImageDTOToImageDB(hit: PSImagesResponseDTO.HitDTO): ImageDB {
        return ImageDB(
            imageId = hit.id,
            tags = hit.tags,
            previewURL = hit.previewURL,
            previewWidth = hit.previewWidth,
            previewHeight = hit.previewHeight,
            largeImageURL = hit.largeImageURL,
            largeImageWidth = hit.imageWidth,
            largeImageHeight = hit.imageHeight,
            likes = hit.likes,
            comments = hit.comments,
            userId = hit.userId,
            user = hit.user,
            userImageURL = hit.userImageURL
        )
    }

    fun mapFromPSImagesResponseDTOToImagesDB(response: PSImagesResponseDTO): List<ImageDB> {
        return response.hits.map { mapFromPSImageDTOToImageDB(it) }
    }

    fun mapFromImageDBToPSImage(imageDb: ImageDB): PSImage {
        return PSImage(
            id = imageDb.imageId,
            thumbSource = ImageData(imageDb.previewURL, imageDb.previewHeight, imageDb.previewWidth),
            username = imageDb.user,
            tags = imageDb.tags.split(",")
        )
    }
}