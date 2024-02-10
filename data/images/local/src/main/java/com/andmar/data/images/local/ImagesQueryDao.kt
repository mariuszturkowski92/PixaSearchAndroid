package com.andmar.data.images.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.andmar.data.images.local.entity.ImageQueryDB
import com.andmar.data.images.local.entity.ImageQueryImageCrossRef
import com.andmar.data.images.local.entity.ImageQueryWithImages

@Dao
internal interface ImagesQueryDao {
    @Transaction
    @Query("SELECT * FROM imagequerydb")
    suspend fun getAll(): List<ImageQueryWithImages>

    @Transaction
    @Query("SELECT * FROM imagequerydb WHERE `query` LIKE :query")
    suspend fun findByQuery(query: String): ImageQueryWithImages

    @Query("SELECT EXISTS(SELECT 1 FROM imagequerydb WHERE `query` = :query)")
    suspend fun exists(query: String): Boolean

    @Insert
    suspend fun insertCrossRef(crossRef: ImageQueryImageCrossRef)

    @Query("DELETE FROM ImageQueryImageCrossRef WHERE query = :query AND imageId = :imageId")
    suspend fun deleteCrossRef(query: String, imageId: Int)

    @Query("SELECT COUNT(*) FROM ImageQueryImageCrossRef WHERE imageId = :imageId")
    suspend fun countCrossRefs(imageId: Int): Int

    @Insert
    suspend fun insert(imageQueryDB: ImageQueryDB)

    @Update
    suspend fun update(imageQueryDB: ImageQueryDB)

    @Delete
    suspend fun delete(imageQueryDB: ImageQueryDB)


}