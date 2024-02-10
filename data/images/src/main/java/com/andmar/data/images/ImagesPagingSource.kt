package com.andmar.data.images

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andmar.data.images.entity.ImagesResult
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.network.ImagesRemoteDataSource
import java.io.IOException

class ImagesPagingSource(
    private val query: String,
    private val remoteDataSource: ImagesRemoteDataSource,
) : PagingSource<Int, PSImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PSImage> {
        return try {
            val currentPage = params.key ?: 1
            val images = remoteDataSource.getImages(
                query = query,
                page = currentPage,
                MAX_PAGE_SIZE
            )
            LoadResult.Page(
                data = ImagesResult.fromDTO(images, currentPage).images,
                prevKey = null, // Only paging forward.
                nextKey = if (images.hits.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PSImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}