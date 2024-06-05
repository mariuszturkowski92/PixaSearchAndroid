package com.andmar.data.images.network

import com.andmar.data.images.network.exceptions.ImageNotFoundException
import com.andmar.data.images.network.exceptions.RateLimitException
import com.andmar.data.images.network.exceptions.UnknownImageException
import com.andmar.data.images.network.model.PSImagesResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import java.io.IOException
import javax.inject.Inject

class ImagesRemoteDataSourceImpl @Inject constructor(
    private val apiKey: String,
    private val client: HttpClient,
) : ImagesRemoteDataSource {


    override suspend fun getImages(
        query: String,
        page: Int,
        maxPageSize: Int
    ): PSImagesResponseDTO {
        return handleExceptions {
            client.get(ImagesRemoteDataSource.BASE_URL) {
                url {
                    parameters.append("key", apiKey)
                    parameters.append("q", query)
                    parameters.append("page", page.toString())
                    parameters.append("per_page", maxPageSize.toString())
                }
            }.body()
        }
    }

    override suspend fun getImageWithId(id: Int): PSImagesResponseDTO {
        return handleExceptions {
            client.get(ImagesRemoteDataSource.BASE_URL) {
                url {
                    parameters.append("key", apiKey)
                    parameters.append("id", id.toString())
                }
            }.body()
        }
    }

    private suspend fun <T> handleExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: ClientRequestException) {
            throw when (e.response.status.value) {
                404 -> ImageNotFoundException(e)
                429 -> RateLimitException(
                    e,
                    e.response.headers["X-RateLimit-Reset"]?.toLong() ?: 0L
                )

                else -> UnknownImageException(e)
            }
        }
    }
}

