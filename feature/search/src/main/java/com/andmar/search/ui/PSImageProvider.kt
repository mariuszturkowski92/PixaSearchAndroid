package com.andmar.search.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.andmar.data.images.entity.ImageData
import com.andmar.data.images.entity.PSImage

internal class PSImageProvider : PreviewParameterProvider<PSImage> {
    override val values: Sequence<PSImage>
        get() = sequenceOf(
            PSImage(
                id = 1,
                thumbSource = ImageData(
                    url = "https://www.example.com/image1",
                    height = 100,
                    width = 100
                ),
                username = "user1",
                tags = listOf("tag1", "tag2"),
                likes = 10,
                comments = 5,
                downloads = 3,
                largeImage = ImageData(
                    url = "https://www.example.com/image1",
                    height = 1024,
                    width = 1024
                )
            )
        )
}