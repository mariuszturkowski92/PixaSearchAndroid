package com.andmar.data.images.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.andmar.data.images.local.mock.ImageWithQueryFactory
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ImagesLocalDataSourceImplTest {

    private lateinit var imagesLocalDataSourceImpl: ImagesLocalDataSourceImpl
    private lateinit var db: ImagesDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ImagesDatabase::class.java).build()
        imagesLocalDataSourceImpl = ImagesLocalDataSourceImpl(db, db.imageWithQueryDao(), db.queryDao())
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun refreshImagesWithQuery_insertsData() = runBlocking {
        val data = ImageWithQueryFactory().createImageWithQueryDBList("testQuery", 1)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(data)

        val count = imagesLocalDataSourceImpl.countWith("testQuery")
        assertEquals(1, count)
    }

    @Test
    fun refreshImagesWithQuery_clearsOldData() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        imageWithQueryFactory.createImageWithQueryDBList("testQuery", 20).let {
            imagesLocalDataSourceImpl.refreshImagesWithQuery(it)
        }

        val newData = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 15)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(newData)

        val count = imagesLocalDataSourceImpl.countWith("testQuery")
        assertEquals(15, count)
    }

    @Test
    fun updateExistingQueryWithImages_updatesData() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        val initialData = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 20)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(initialData)

        val newData = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 20)
        imagesLocalDataSourceImpl.updateExistingQueryWIthImages(newData, 2)

        val count = imagesLocalDataSourceImpl.countWith("testQuery")
        assertEquals(40, count)
    }

    @Test
    fun deleteOldestIfCountExceedCacheLimit_deletesData() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        (1..(MAX_QUERIES_CACHED + 2)).forEach {
            val data = imageWithQueryFactory.createImageWithQueryDBList("testQuery$it", 20)
            imagesLocalDataSourceImpl.refreshImagesWithQuery(data)
        }

        imagesLocalDataSourceImpl.deleteOldestIfCountExceedCacheLimit()

        val count = imagesLocalDataSourceImpl.countWith("testQuery1")
        assertEquals(0, count)
    }

    @Test
    fun getCurrentPage_returnsCorrectPage() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        val data = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 1)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(data)

        imagesLocalDataSourceImpl.updateExistingQueryWIthImages(
            imageWithQueryFactory.createImageWithQueryDBList(
                "testQuery",
                1
            ), 2
        )

        val page = imagesLocalDataSourceImpl.getCurrentPage("testQuery")
        assertEquals(2, page)
    }
}