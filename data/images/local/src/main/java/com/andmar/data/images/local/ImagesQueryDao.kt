package com.andmar.data.images.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.andmar.data.images.local.entity.ImageDB
import com.andmar.data.images.local.entity.ImageQueryDB
import com.andmar.data.images.local.entity.ImageQueryImageCrossRef
import com.andmar.data.images.local.entity.ImageQueryWithImages

@Dao
internal interface ImagesQueryDao {

    @Query("SELECT COUNT(*) FROM imagequerydb")
    suspend fun count(): Int

    @Transaction
    @Query("SELECT * FROM imagequerydb")
    suspend fun getAll(): List<ImageQueryWithImages>

    @Query("SELECT * FROM imagequerydb ORDER BY modified_at LIMIT :limit OFFSET $MAX_QUERY_CACHED")
    suspend fun selectOldest(limit: Int): List<ImageQueryWithImages>

    @Transaction
    @Query("SELECT * FROM imagequerydb WHERE `query` LIKE :query")
    suspend fun findByQuery(query: String): ImageQueryWithImages

    @Query("SELECT EXISTS(SELECT 1 FROM imagequerydb WHERE `query` = :query)")
    suspend fun exists(query: String): Boolean

    @Query(
        """
        SELECT ImageDB.* 
        FROM ImageDB 
        JOIN ImageQueryImageCrossRef ON ImageDB.imageId = ImageQueryImageCrossRef.imageId 
        JOIN ImageQueryDB ON ImageQueryImageCrossRef.`query` = ImageQueryDB.`query` 
        WHERE ImageQueryDB.`query` = :query
    """
    )
    fun pagingSource(query: String): PagingSource<Int, ImageDB>

    @Insert
    suspend fun insertCrossRef(crossRef: ImageQueryImageCrossRef)

    @Query("DELETE FROM ImageQueryImageCrossRef WHERE `query` = :query AND imageId = :imageId")
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