package com.andmar.data.images.network.exceptions

import java.io.IOException

class ImageNotFoundException(source: Throwable) : IOException(source)
class RateLimitException(source: Throwable, val waitingTime: Long) : IOException(source)
class UnknownImageException(source: Throwable) : IOException(source)