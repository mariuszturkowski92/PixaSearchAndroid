package com.andmar.data.images.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andmar.data.images.local.entity.ImageWithQueryDB
import com.andmar.data.images.local.entity.QueryDB


@Database(
    entities = [ImageWithQueryDB::class, QueryDB::class],
    version = 1
)
internal abstract class ImagesDatabase : RoomDatabase() {

    abstract fun ImageWithQueryDao(): ImageWithQueryDao

    abstract fun queryDao(): QueryDao

    companion object {
        fun create(context: Context): ImagesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ImagesDatabase::class.java,
                "images.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}