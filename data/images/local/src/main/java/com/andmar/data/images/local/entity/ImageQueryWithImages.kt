package com.andmar.data.images.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ImageQueryWithImages(
    @Embedded
    val imageQueryDB: ImageQueryDB,
    @Relation(
        parentColumn = "query",
        entityColumn = "imageId",
        associateBy = Junction(ImageQueryImageCrossRef::class)
    )
    val images: List<ImageDB>,
)