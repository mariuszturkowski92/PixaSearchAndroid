package com.andmar.data.images.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageDB(
    @PrimaryKey val id: Int,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val largeImageURL: String,
    val largeImageWidth: Int,
    val largeImageHeight: Int,
    val user: String,
    val userImageURL: String,
    val likes: Int,
    val favorites: Int,
    val comments: Int,
    val tags: String,
)
