package com.andmar.search.ui

import androidx.compose.runtime.Stable
import java.util.Optional

internal data class SearchScreenInput(
    val query: String,
    val showDialog: Optional<ImageItem> = Optional.empty(),
)