package com.andmar.data.images.entity

import androidx.annotation.VisibleForTesting

data class PSImage(val id: Int, val thumbSource: ImageData, val username: String, val tags: List<String>) {
    companion object {
        @VisibleForTesting
        fun testModel(id: Int) =
            PSImage(id, ImageData("https://www.google.com", 100, 100), "test", listOf("test"))
    }
}


data class ImageData(val url: String, val height: Int, val width: Int)