package com.andmar.data.images.local.mock

import androidx.annotation.VisibleForTesting
import com.andmar.data.images.local.entity.ImageWithQueryDB

@VisibleForTesting
class ImageWithQueryFactory {
    var lastImageId = 0

    // create a list of ImageWithQueryDB given a query and a number of images
    fun createImageWithQueryDBList(query: String, numberOfImages: Int): List<ImageWithQueryDB> {
        return (1..numberOfImages).map {
            val imageId = lastImageId + it
            ImageWithQueryDB(
                imageId = imageId,
                query = query,
                previewURL = "testUrl$imageId",
                previewWidth = imageId,
                previewHeight = imageId,
                largeImageURL = "largeTestUrl$imageId",
                largeImageWidth = imageId,
                largeImageHeight = imageId,
                user = "testUser$imageId",
                userImageURL = "userImageUrl$imageId",
                likes = imageId,
                comments = imageId,
                tags = "testTag$imageId",
                userId = imageId,
                downloads = 23
            )
        }.apply {
            lastImageId += numberOfImages
        }

    }
}