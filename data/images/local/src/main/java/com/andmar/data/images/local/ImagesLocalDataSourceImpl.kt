package com.andmar.data.images.local

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.andmar.common.utils.IoDispatcher
import com.andmar.data.images.local.ImagesLocalDataSource.Companion.MAX_EMPTY_QUERIES_IMAGES_CACHED
import com.andmar.data.images.local.ImagesLocalDataSource.Companion.MAX_QUERIES_CACHED
import com.andmar.data.images.local.entity.ImageWithQueryDB
import com.andmar.data.images.local.entity.QueryDB
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class ImagesLocalDataSourceImpl @Inject constructor(
    private val database: ImagesDatabase,
    private val imageWithQueryDao: ImageWithQueryDao,
    private val queryDao: QueryDao,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
) : ImagesLocalDataSource {

    override suspend fun deleteOldestIfCountExceedCacheLimit() {
        val count = queryDao.count()
        if (count > MAX_QUERIES_CACHED) {
            val oldestImageQueryWithImagesList = queryDao.selectOldest(count - MAX_QUERIES_CACHED)
            oldestImageQueryWithImagesList.forEach {
                deleteAllImagesWithQuery(it)
            }
        }

        // delete images that was cached during fetching image by id, In this case query could be not provided.
        val emptyQueryImagesCount = imageWithQueryDao.countWith(ImagesLocalDataSource.EMPTY_QUERY)
        if (emptyQueryImagesCount > MAX_EMPTY_QUERIES_IMAGES_CACHED) {
            val oldestEmptyQueryImages = imageWithQueryDao.selectOldestEmptyQueryImages(
                limit = emptyQueryImagesCount - MAX_EMPTY_QUERIES_IMAGES_CACHED
            )
            oldestEmptyQueryImages.forEach {
                imageWithQueryDao.delete(it)
            }
        }
    }

    override suspend fun isImageExists(id: Int): Boolean {
        return imageWithQueryDao.isImageExists(id)
    }

    override fun getImageWithId(id: Int): Flow<ImageWithQueryDB> {
        return imageWithQueryDao.getByIdFlow(id)
    }

    override suspend fun updateSingleImage(imageWithQueryDB: ImageWithQueryDB) {
        imageWithQueryDB.imageId.let {
            val currentImageWithQuery = imageWithQueryDao.getById(it)
            if (currentImageWithQuery == null) {
                imageWithQueryDao.insert(imageWithQueryDB)
            } else {
                imageWithQueryDao.update(
                    imageWithQueryDB.copy(
                        id = currentImageWithQuery.id,
                        query = currentImageWithQuery.query,
                        modifiedAt = System.currentTimeMillis()
                    )
                )
            }
        }

    }

    override fun getImagesWithQueryPagingSource(query: String): PagingSource<Int, ImageWithQueryDB> {
        return imageWithQueryDao.getByQueryPagingSource(query)
    }

    override suspend fun refreshImagesWithQuery(data: List<ImageWithQueryDB>) {
        if (data.map { it.query }.distinct().size > 1) {
            throw IllegalArgumentException("All images must have the same query")
        }
        val query = data.first().query
        database.withTransaction {
            imageWithQueryDao.deleteAllWithQuery(query)
            imageWithQueryDao.insertAll(data)
            queryDao.insert(QueryDB(query, 1))
        }
    }

    override suspend fun updateExistingQueryWIthImages(data: List<ImageWithQueryDB>, page: Int) {
        withContext(ioDispatcher) {
            if (data.map { it.query }.distinct().size > 1) {
                throw IllegalArgumentException("All images must have the same query")
            }
            database.withTransaction {
                imageWithQueryDao.insertAll(data)
                queryDao.updatePage(query = data.first().query, page = page)
            }
        }
    }

    override suspend fun countWith(query: String): Int {
        return imageWithQueryDao.countWith(query)
    }

    override suspend fun getCurrentPage(query: String): Int {
        return queryDao.getQueryByParam(query)?.lastPage ?: 0
    }

    private suspend fun deleteAllImagesWithQuery(queryDB: QueryDB) {
        database.withTransaction {
            imageWithQueryDao.deleteAllWithQuery(queryDB.query)
            queryDao.deleteQueryByParam(queryDB.query)
        }
    }
}
