package com.andmar.data.images.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.andmar.data.images.local.entity.ImageDB

@Dao
internal interface ImageDBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageDB>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateImages(images: List<ImageDB>)

    @Delete
    suspend fun deleteImages(images: List<ImageDB>)
}