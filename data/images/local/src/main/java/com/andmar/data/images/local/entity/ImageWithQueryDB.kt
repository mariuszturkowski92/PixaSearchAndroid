package com.andmar.data.images.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(
        value = arrayOf("imageId", "query"),
        unique = true
    ), Index("query")]
)
data class ImageWithQueryDB(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val imageId: Int,
    val query: String,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val largeImageURL: String,
    val largeImageWidth: Int,
    val largeImageHeight: Int,
    val user: String,
    val userImageURL: String,
    val likes: Int,
    val comments: Int,
    val downloads: Int,
    val tags: String,
    val userId: Int,
)
