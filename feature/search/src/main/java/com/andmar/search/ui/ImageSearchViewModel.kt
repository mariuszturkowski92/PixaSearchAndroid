package com.andmar.search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.andmar.data.images.entity.ImagesResult
import com.andmar.data.images.usecase.GetImagesUseCase
import com.andmar.ui.state.StateViewModel
import com.andmar.ui.state.State
import com.andmar.ui.state.launchWithErrorHandlingOn
import com.andmar.ui.state.success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
internal class ImageSearchViewModel @Inject constructor(
    val handle: SavedStateHandle,
    private val getImagesUseCase: GetImagesUseCase,
) : StateViewModel<ImagesResult>() {

    private val _input = MutableStateFlow(SearchScreenInput(DEFAULT_SEARCH_QUERY))
    val input: MutableStateFlow<SearchScreenInput>
        get() = _input

    init {
        searchForImages()
    }

    fun searchForImages() {
        _uiState.launchWithErrorHandlingOn(viewModelScope, showLoading = true) {
            getImagesUseCase(_input.value.query, 1).collect {
                _uiState.success(it)
            }
        }
    }

    fun onNewQuery(newQuery: String) {
        _input.value = _input.value.copy(query = newQuery)
    }

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "fruits"
        private const val TAG_SEARCH_FOR_IMAGES = "search_for_images"
    }

}