package com.andmar.search.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.andmar.data.images.entity.PSImage

internal class PSImageProvider : PreviewParameterProvider<PSImage> {
    override val values: Sequence<PSImage>
        get() = sequenceOf(
            PSImage(
                id = "1",
                thumbUrl = "https://images.unsplash.com/photo-1629353949943-3e3e3e3e3e3e",
                username = "user1",
                tags = listOf("tag1", "tag2"),
            )
        )
}