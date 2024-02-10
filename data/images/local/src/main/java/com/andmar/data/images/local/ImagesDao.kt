package com.andmar.data.images.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.andmar.data.images.local.entity.ImageQueryDB

@Dao
internal interface ImagesDao {
    @Query("SELECT * FROM imagequerydb")
    suspend fun getAll(): List<ImageQueryDB>

    @Query("SELECT * FROM imagequerydb WHERE `query` LIKE :query")
    suspend fun findByQuery(query: String): ImageQueryDB

    @Insert
    suspend fun insertAll(vararg imageQueryDB: ImageQueryDB)

    @Update
    suspend fun update(imageQueryDB: ImageQueryDB)

    @Delete
    suspend fun delete(imageQueryDB: ImageQueryDB)

}