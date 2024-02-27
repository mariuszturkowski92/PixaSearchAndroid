package com.andmar.search.ui

import androidx.compose.runtime.Stable
import com.andmar.search.ui.paging.ImageItem
import java.util.Optional

@Stable
internal data class SearchScreenInput(
    val query: String,
    val showDialog: Optional<ImageItem> = Optional.empty(),
)