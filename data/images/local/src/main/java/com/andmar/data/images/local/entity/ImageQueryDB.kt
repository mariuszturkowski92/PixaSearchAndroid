package com.andmar.data.images.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageQueryDB(
    @PrimaryKey val query: String,
    @ColumnInfo(name = "fetched_pages") val fetchedPages: Int,
    @ColumnInfo(name = "total_results") val totalResults: Int,
    @ColumnInfo(
        name = "created_at",
        defaultValue = "CURRENT_TIMESTAMP"
    ) val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(
        name = "modified_at",
        defaultValue = "CURRENT_TIMESTAMP"
    ) val modifiedAt: Long = System.currentTimeMillis(),
)
