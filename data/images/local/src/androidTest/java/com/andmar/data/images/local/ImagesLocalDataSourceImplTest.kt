package com.andmar.data.images.local

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.hasSameSizeAs
import assertk.assertions.isEqualToIgnoringGivenProperties
import com.andmar.data.images.local.entity.ImageWithQueryDB
import com.andmar.data.images.local.mock.ImageWithQueryFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

        (1..(ImagesLocalDataSource.MAX_QUERIES_CACHED + 2)).forEach {
            val data = imageWithQueryFactory.createImageWithQueryDBList("testQuery$it", 20)
            imagesLocalDataSourceImpl.refreshImagesWithQuery(data)
        }

        repeat((1..(ImagesLocalDataSource.MAX_EMPTY_QUERIES_IMAGES_CACHED + 20)).count()) {
            val data = imageWithQueryFactory.createImageWithQueryDBList(ImagesLocalDataSource.EMPTY_QUERY, 1)
            imagesLocalDataSourceImpl.updateSingleImage(data.first())
        }

        imagesLocalDataSourceImpl.deleteOldestIfCountExceedCacheLimit()

        val count = imagesLocalDataSourceImpl.countWith("testQuery1")
        assertEquals(0, count)
        assertEquals(
            ImagesLocalDataSource.MAX_EMPTY_QUERIES_IMAGES_CACHED,
            imagesLocalDataSourceImpl.countWith(ImagesLocalDataSource.EMPTY_QUERY)
        )
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

    @Test
    fun isImageExists_returnsCorrectResult() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        val data = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 1)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(data)

        val exists = imagesLocalDataSourceImpl.isImageExists(data.first().imageId)
        assertTrue(exists)
    }

    @Test
    fun getImageWithId_returnsCorrectImage() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        val data = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 1)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(data)

        val image = imagesLocalDataSourceImpl.getImageWithId(data.first().imageId).first()
        assertEquals(data.first().imageId, image.imageId)
    }

    @Test
    fun updateSingleImage_updatesCorrectly() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        val data = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 1)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(data)


        val updatedImage = data.first().copy(downloads = 230)
        imagesLocalDataSourceImpl.updateSingleImage(updatedImage)

        val image = imagesLocalDataSourceImpl.getImageWithId(updatedImage.imageId).first()
        assertTrue(data.first().modifiedAt < image.modifiedAt)
        assertEquals(image.downloads, updatedImage.downloads)
    }

    @Test
    fun getImagesWithQueryPagingSource_returnsCorrectPagingSource() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        val data = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 20)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(data)

        val pagingSource = imagesLocalDataSourceImpl.getImagesWithQueryPagingSource("testQuery")
        val loadResult: PagingSource.LoadResult<Int, ImageWithQueryDB> =
            pagingSource.load(PagingSource.LoadParams.Refresh(null, 20, false))

        assertTrue(loadResult is PagingSource.LoadResult.Page)
        assertThat((loadResult as PagingSource.LoadResult.Page).data).hasSameSizeAs(data)
        loadResult.data.forEachIndexed { index, imageWithQueryDB ->
            assertThat(imageWithQueryDB).isEqualToIgnoringGivenProperties(
                data[index],
                ImageWithQueryDB::modifiedAt,
                ImageWithQueryDB::id
            )
        }
    }

    @Test
    fun deleteOldestIfCountExceedCacheLimit_deletesCorrectly() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        (1..(ImagesLocalDataSource.MAX_QUERIES_CACHED + 2)).forEach {
            val data = imageWithQueryFactory.createImageWithQueryDBList("testQuery$it", 20)
            imagesLocalDataSourceImpl.refreshImagesWithQuery(data)
        }

        imagesLocalDataSourceImpl.deleteOldestIfCountExceedCacheLimit()

        val count = imagesLocalDataSourceImpl.countWith("testQuery1")
        assertEquals(0, count)
    }

    @Test
    fun updateSingleImage_preservesOrder() = runBlocking {
        val imageWithQueryFactory = ImageWithQueryFactory()

        // Step 1: Create and insert a list of images
        val initialData = imageWithQueryFactory.createImageWithQueryDBList("testQuery", 20)
        imagesLocalDataSourceImpl.refreshImagesWithQuery(initialData)

        // Step 2: Update one of the images
        val updatedImage = initialData[10].copy(downloads = 230)
        imagesLocalDataSourceImpl.updateSingleImage(updatedImage)

        // Step 3: Retrieve the list of images from the database
        val pagingSource = imagesLocalDataSourceImpl.getImagesWithQueryPagingSource("testQuery")
        val loadResult: PagingSource.LoadResult<Int, ImageWithQueryDB> =
            pagingSource.load(PagingSource.LoadParams.Refresh(null, 20, false))

        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val retrievedData = (loadResult as PagingSource.LoadResult.Page).data

        // Step 4: Check if the order of the images is the same
        initialData.forEachIndexed { index, imageWithQueryDB ->
            assertThat(retrievedData[index]).isEqualToIgnoringGivenProperties(
                imageWithQueryDB,
                ImageWithQueryDB::modifiedAt,
                ImageWithQueryDB::downloads,
                ImageWithQueryDB::id
            )
        }
    }
}