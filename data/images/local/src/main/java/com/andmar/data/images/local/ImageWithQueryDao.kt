package com.andmar.data.images.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.andmar.data.images.local.entity.ImageWithQueryDB
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ImageWithQueryDao {

    @Query("SELECT * FROM ImageWithQueryDB")
    suspend fun getAll(): List<ImageWithQueryDB>

    @Query("SELECT * FROM ImageWithQueryDB WHERE imageId = :id")
    suspend fun getById(id: Int): ImageWithQueryDB
    @Query("SELECT EXISTS(SELECT * FROM ImageWithQueryDB WHERE imageId = :id)")
    suspend fun isImageExists(id: Int): Boolean

    @Query("SELECT * FROM ImageWithQueryDB WHERE `query` = :query")
    fun getByQueryFlow(query: String): Flow<List<ImageWithQueryDB>>

    @Query("SELECT * FROM ImageWithQueryDB WHERE `query` = :query ORDER BY id ASC")
    fun getByQueryPagingSource(query: String): PagingSource<Int, ImageWithQueryDB>

    // delete all with query
    @Query("DELETE FROM ImageWithQueryDB WHERE `query` = :query")
    suspend fun deleteAllWithQuery(query: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(imageWithQueryDBs: List<ImageWithQueryDB>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(imageWithQueryDB: ImageWithQueryDB)

    @Update
    suspend fun update(imageWithQueryDB: ImageWithQueryDB)

    @Delete
    suspend fun delete(imageWithQueryDB: ImageWithQueryDB)

    @Query("SELECT COUNT(*) FROM ImageWithQueryDB WHERE `query` = :query")
    suspend fun countWith(query: String): Int

    @Query("SELECT * FROM ImageWithQueryDB WHERE imageId = :id")
    fun getByIdFlow(id: Int): Flow<ImageWithQueryDB>
}