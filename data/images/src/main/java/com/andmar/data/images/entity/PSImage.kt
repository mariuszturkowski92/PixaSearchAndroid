package com.andmar.data.images.entity

import androidx.annotation.VisibleForTesting


data class PSImage(
    val id: Long,
    val pixaId: Int,
    val thumbSource: ImageData,
    val largeImage: ImageData,
    val username: String,
    val tags: List<String>,
    val likes: Int,
    val comments: Int,
    val downloads: Int,
) {
    companion object {

        @VisibleForTesting
        fun testModel(id: Int) =
            PSImage(
                id.toLong(),
                id,
                ImageData("https://www.google.com", 150, 120),
                ImageData(
                    "https://pixabay.com/get/g5e32e568ad751e97e5b34f7a99b79af05afd271440e5aac7688fd135026be24ccacaf2f46c8c4dd60eef96928c67b494c1f4aad745a8a2e0574c6073fe854c4d_1280.jpg",
                    2000,
                    1333
                ),
                "test",
                (1..10).map { "tag$it" },
                (1..100).random(),
                (1..100).random(),
                (1..100).random(),
            )
    }
}


data class ImageData(val url: String, val height: Int, val width: Int)