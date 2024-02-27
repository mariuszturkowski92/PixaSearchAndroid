package com.andmar.search.ui.paging

import androidx.compose.runtime.Immutable
import com.andmar.data.images.entity.ImageData

@Immutable
data class ImageItem(
    val id: Int,
    val thumbSource: ImageData,
    val largeImage: ImageData,
    val username: String,
    val tags: List<String>,
    val likes: Int,
    val comments: Int,
    val downloads: Int,
) {
    companion object {
        fun fromPSImage(psImage: com.andmar.data.images.entity.PSImage): ImageItem {
            return ImageItem(
                id = psImage.id,
                thumbSource = psImage.thumbSource,
                largeImage = psImage.largeImage,
                username = psImage.username,
                tags = psImage.tags,
                likes = psImage.likes,
                comments = psImage.comments,
                downloads = psImage.downloads
            )
        }
    }
}
