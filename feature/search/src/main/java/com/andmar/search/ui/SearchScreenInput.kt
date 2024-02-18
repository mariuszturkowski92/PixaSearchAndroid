package com.andmar.search.ui

import com.andmar.data.images.entity.PSImage
import java.util.Optional

internal data class SearchScreenInput(val query: String, val showDialog: Optional<PSImage> = Optional.empty())