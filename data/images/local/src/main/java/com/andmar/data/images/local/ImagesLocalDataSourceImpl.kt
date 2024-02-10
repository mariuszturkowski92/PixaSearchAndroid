package com.andmar.data.images.local

import com.andmar.data.images.local.entity.ImageQueryImageCrossRef
import com.andmar.data.images.local.entity.ImageQueryWithImages
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImagesLocalDataSourceImpl @Inject constructor(
    private val imageQueryDao: ImagesQueryDao,
    private val imageDBDao: ImageDBDao,
) :
    ImagesLocalDataSource {
    override suspend fun getImageQuery(query: String): ImageQueryWithImages? {
        if (!imageQueryDao.exists(query)) {
            return null
        }
        return imageQueryDao.findByQuery(query)
    }

    override suspend fun insertOrUpdateImageQueryWithImages(imageQueryWithImages: ImageQueryWithImages) {
        if (!imageQueryDao.exists(imageQueryWithImages.imageQueryDB.query)) {
            insertImageQueryWithImages(imageQueryWithImages)
        } else {
            updateImageQueryWithImages(imageQueryWithImages)
        }
    }

    private suspend fun insertImageQueryWithImages(imageQueryWithImages: ImageQueryWithImages) {
        imageQueryDao.insert(imageQueryWithImages.imageQueryDB)
        imageDBDao.insertImages(imageQueryWithImages.images)
        imageQueryWithImages.images.forEach { image ->
            imageQueryDao.insertCrossRef(
                ImageQueryImageCrossRef(
                    imageQueryWithImages.imageQueryDB.query,
                    image.imageId
                )
            )
        }
    }

    private suspend fun updateImageQueryWithImages(imageQueryWithImages: ImageQueryWithImages) {
        // Step 1: Retrieve the current ImageQueryWithImages from the database
        val currentImageQueryWithImages = imageQueryDao.findByQuery(imageQueryWithImages.imageQueryDB.query)

        // Step 2: Compare the images
        val imagesToDelete = currentImageQueryWithImages.images - imageQueryWithImages.images.toSet()
        val imagesToInsert = imageQueryWithImages.images - currentImageQueryWithImages.images.toSet()

        // Step 3: Delete the images that are in the database but not in the new ImageQueryWithImages
        if (imagesToDelete.isNotEmpty()) {
            imageDBDao.deleteImages(imagesToDelete)
            imagesToDelete.forEach { image ->
                imageQueryDao.deleteCrossRef(imageQueryWithImages.imageQueryDB.query, image.imageId)
            }
        }

        // Step 4: Insert the new images that are in the new ImageQueryWithImages but not in the database
        if (imagesToInsert.isNotEmpty()) {
            imageDBDao.insertImages(imagesToInsert)
            imagesToInsert.forEach { image ->
                imageQueryDao.insertCrossRef(
                    ImageQueryImageCrossRef(
                        imageQueryWithImages.imageQueryDB.query,
                        image.imageId
                    )
                )
            }
        }

        // Update the ImageQueryDB
        imageQueryDao.update(imageQueryWithImages.imageQueryDB)
    }

    suspend fun deleteImageQueryWithImages(imageQueryWithImages: ImageQueryWithImages) {
        // Delete the ImageQueryDB
        imageQueryDao.delete(imageQueryWithImages.imageQueryDB)

        // Delete the ImageQueryImageCrossRef entries
        imageQueryWithImages.images.forEach { image ->
            imageQueryDao.deleteCrossRef(imageQueryWithImages.imageQueryDB.query, image.imageId)
        }

        // For each image, check if there are any remaining references in the ImageQueryImageCrossRef table
        imageQueryWithImages.images.forEach { image ->
            val remainingReferences = imageQueryDao.countCrossRefs(image.imageId)
            if (remainingReferences == 0) {
                // If there are no remaining references, delete the image
                imageDBDao.deleteImages(listOf(image))
            }
        }
    }
}