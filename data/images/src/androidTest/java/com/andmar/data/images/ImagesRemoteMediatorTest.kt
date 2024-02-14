package com.andmar.data.images

import androidx.paging.ExperimentalPagingApi
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andmar.data.images.local.mock.TestImagesLocalDataSource
import com.andmar.data.images.network.mock.ImagesFactory
import com.andmar.data.images.network.mock.MockImageRemoteDataSource
import com.andmar.data.images.network.model.PSImagesResponseDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.runner.RunWith


@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PageKeyedRemoteMediatorTest {
    private val imagesFactory = ImagesFactory()
    private val mockImagesResponse = listOf(
        imagesFactory.createImageRemoteResponse(MAX_PAGE_SIZE),
        imagesFactory.createImageRemoteResponse(MAX_PAGE_SIZE),
        imagesFactory.createImageRemoteResponse(MAX_PAGE_SIZE)
    )
    private val mockApi = MockImageRemoteDataSource().apply {
        mockImagesResponse.forEach { addImageResponse(it) }
    }

    private fun addImageResponse(psImagesResponseDTO: PSImagesResponseDTO) {
        mockApi.imagesResponse.add(psImagesResponseDTO)
    }


    private val testImagesLocalDataSource = TestImagesLocalDataSource()


    @After
    fun tearDown() {
//        testImagesLocalDataSource.clear()
        // Clear out failure message to default to the successful response.
        mockApi.failureMsg = null
        // Clear out posts after each test run.
        mockApi.clearPosts()
    }

    // on refresh start from page 1 with correct query

}

