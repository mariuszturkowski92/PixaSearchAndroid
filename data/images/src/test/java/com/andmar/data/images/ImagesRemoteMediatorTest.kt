package com.andmar.data.images

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.RemoteMediator
import com.andmar.data.images.local.ImagesLocalDataSource
import com.andmar.data.images.network.ImagesRemoteDataSource
import com.andmar.data.images.network.mock.ImagesFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class ImagesRemoteMediatorTest {

    @Test
    fun `refresh clears and adds new data`() = runTest {
        // Given
        val query = "testQuery"
        val expectedPage = 1
        val testDispatcher = StandardTestDispatcher(this.testScheduler)
        val imagesLocalDataSource = mockk<ImagesLocalDataSource>(relaxed = true)
        val imagesRemoteDataSource = mockk<ImagesRemoteDataSource>()
        val mediator =
            ImagesRemoteMediator(query, imagesLocalDataSource, imagesRemoteDataSource, testDispatcher)

        // Mocking the getImages method of ImagesRemoteDataSource
        coEvery { imagesRemoteDataSource.getImages(any(), any(), any()) } answers {
            Assert.assertEquals(query, args[0])
            assertEquals(expectedPage, args[1])
            ImagesFactory().createImageRemoteResponse(args[2] as Int)
        }

        // When
        mediator.load(LoadType.REFRESH, mockk())

        // Then
        coVerify { imagesLocalDataSource.refreshImagesWithQuery(any()) }
    }

    @Test
    fun `mediator stops loading when last load returns empty list`() = runTest {
        // Given
        val query = "testQuery"
        val imagesLocalDataSource = mockk<ImagesLocalDataSource>(relaxed = true)
        val imagesRemoteDataSource = mockk<ImagesRemoteDataSource>()
        val mediator = ImagesRemoteMediator(
            query,
            imagesLocalDataSource,
            imagesRemoteDataSource,
            StandardTestDispatcher(this.testScheduler)
        )

        // Mocking the getImages method of ImagesRemoteDataSource to return an empty list
        coEvery { imagesRemoteDataSource.getImages(any(), any(), any()) } returns mockk(relaxed = true) {
            ImagesFactory().createImageRemoteResponse(0)
        }

        // When
        val result = mediator.load(LoadType.APPEND, mockk())

        // Then
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `load correct next page on append load`() = runTest {
        // Given
        val query = "testQuery"
        val imagesLocalDataSource = mockk<ImagesLocalDataSource>(relaxed = true)
        val imagesRemoteDataSource = mockk<ImagesRemoteDataSource>()
        val mediator = ImagesRemoteMediator(
            query,
            imagesLocalDataSource,
            imagesRemoteDataSource,
            StandardTestDispatcher(this.testScheduler)
        )

        // Mocking the getCurrentPage method of ImagesLocalDataSource to return a specific page
        coEvery { imagesLocalDataSource.getCurrentPage(any()) } returns 1

        // Mocking the getImages method of ImagesRemoteDataSource
        coEvery { imagesRemoteDataSource.getImages(any(), any(), any()) } answers {
            assertEquals(query, args[0])
            assertEquals(2, args[1]) // Expecting the next page (2) after the current page (1)
            ImagesFactory().createImageRemoteResponse(args[2] as Int)
        }

        // When
        mediator.load(LoadType.APPEND, mockk())
    }

}