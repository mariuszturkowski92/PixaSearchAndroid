package com.andmar.data.images.entity

data class PSImage(val id: String, val thumbSource: ImageData, val username: String, val tags: List<String>)


data class ImageData(val url: String, val height: Int, val width: Int)