package com.andmar.data.images.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["query", "imageId"],
    indices = [Index("query")],
)
data class ImageQueryImageCrossRef(
    val query: String,
    val imageId: Int,
)
