package com.andmar.data.images.local

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class ImagesLocalDataSourceImplTest {

    private lateinit var imageQueryDao: ImagesQueryDao
    private lateinit var imageDBDao: ImageDBDao
    private lateinit var imagesLocalDataSourceImpl: ImagesLocalDataSourceImpl

    @Before
    fun setUp() {
        imageQueryDao = mockk(relaxed = true)
        imageDBDao = mockk(relaxed = true)
        imagesLocalDataSourceImpl = ImagesLocalDataSourceImpl(imageQueryDao, imageDBDao)
    }

    @Test
    fun `insertOrUpdateImageQueryWithImages inserts new query when not exists`() = runTest {
        val imageQueryWithImages = ImageQueryWithImages(ImageQueryDB("test", 2, totalResults = 23), listOf())

        coEvery { imageQueryDao.exists(imageQueryWithImages.imageQueryDB.query) } returns false

        imagesLocalDataSourceImpl.insertOrUpdateImageQueryWithImages(imageQueryWithImages)

        coVerify { imageQueryDao.insert(imageQueryWithImages.imageQueryDB) }
    }

    @Test
    fun `insertOrUpdateImageQueryWithImages updates existing query when exists`() = runTest {
        val imageQueryWithImages = ImageQueryWithImages(ImageQueryDB("test", 2, totalResults = 23), listOf())
        val existingImageQueryWithImages =
            ImageQueryWithImages(ImageQueryDB("test", 2, totalResults = 23), listOf())

        coEvery { imageQueryDao.exists(imageQueryWithImages.imageQueryDB.query) } returns true
        coEvery { imageQueryDao.findByQuery(imageQueryWithImages.imageQueryDB.query) } returns existingImageQueryWithImages

        imagesLocalDataSourceImpl.insertOrUpdateImageQueryWithImages(imageQueryWithImages)

        coVerify { imageQueryDao.update(any()) }
    }
}
