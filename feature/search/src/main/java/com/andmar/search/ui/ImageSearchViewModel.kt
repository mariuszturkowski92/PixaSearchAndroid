package com.andmar.search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andmar.data.images.entity.ImagesResult
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.usecase.GetImagesUseCase
import com.andmar.data.images.usecase.GetImagesWithPagingUseCase
import com.andmar.ui.state.StateViewModel
import com.andmar.ui.state.launchWithErrorHandling
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class ImageSearchViewModel @Inject constructor(
    val handle: SavedStateHandle,
    private val getImagesUseCase: GetImagesUseCase,
    private val getImagesWithPagingUseCase: GetImagesWithPagingUseCase,
) : StateViewModel<ImagesResult>() {

    private val _input = MutableStateFlow(SearchScreenInput(DEFAULT_SEARCH_QUERY))
    val input: StateFlow<SearchScreenInput>
        get() = _input

    private val _pagingData: MutableStateFlow<PagingData<PSImage>> = MutableStateFlow(PagingData.empty())
    val pagingData: StateFlow<PagingData<PSImage>>
        get() = _pagingData

    init {
//        searchForImages()
    }

    fun searchForImages() {
        viewModelScope.launchWithErrorHandling {
            getImagesWithPagingUseCase(_input.value.query).cachedIn(viewModelScope)
                .collect {
                    _pagingData.value = it
                }
        }
//        _uiState.launchWithErrorHandlingOn(viewModelScope, showLoading = true) {
//            getImagesUseCase(_input.value.query, 1).collect {
//                _uiState.success(it)
//            }
//        }
    }

    fun onNewQuery(newQuery: String) {
        _input.value = _input.value.copy(query = newQuery)
    }

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "fruits"
        private const val TAG_SEARCH_FOR_IMAGES = "search_for_images"
    }

}