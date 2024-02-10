package com.andmar.data.images.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andmar.data.images.local.entity.ImageDB
import com.andmar.data.images.local.entity.ImageQueryDB


@Database(entities = [ImageDB::class, ImageQueryDB::class], version = 1)
internal abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImagesDao
}