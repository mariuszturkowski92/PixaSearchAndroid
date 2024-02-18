package com.andmar.data.images.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andmar.data.images.local.ImagesLocalDataSource.Companion.MAX_QUERIES_CACHED
import com.andmar.data.images.local.entity.QueryDB

@Dao
internal interface QueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(queryDB: QueryDB)

    @Query("SELECT * FROM QueryDB WHERE `query` = :queryParam")
    suspend fun getQueryByParam(queryParam: String): QueryDB?

    @Query("DELETE FROM QueryDB WHERE `query` = :queryParam")
    suspend fun deleteQueryByParam(queryParam: String)

    @Query("UPDATE QueryDB SET lastPage = :page, modified_at = CURRENT_TIMESTAMP WHERE `query` = :query")
    suspend fun updatePage(query: String, page: Int)

    @Query("SELECT * FROM QueryDB ORDER BY modified_at DESC LIMIT :limit OFFSET $MAX_QUERIES_CACHED")
    suspend fun selectOldest(limit: Int): List<QueryDB>

    @Query("SELECT COUNT(*) FROM QueryDB")
    fun count(): Int

}
