package com.andmar.search.ui.paging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.usecase.GetImagesWithPagingUseCase
import com.andmar.search.ui.SearchScreenInput
import com.andmar.ui.state.launchWithErrorHandling
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class ImageSearchPagingViewModel @Inject constructor(
    val handle: SavedStateHandle,
    private val getImagesWithPagingUseCase: GetImagesWithPagingUseCase,
) : ViewModel() {

    private val _input = MutableStateFlow(SearchScreenInput(DEFAULT_SEARCH_QUERY))
    val input: StateFlow<SearchScreenInput>
        get() = _input

    private val _pagingData: MutableStateFlow<PagingData<PSImage>> = MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<PSImage>>
        get() = _pagingData

    init {
        searchForImages()
    }

    fun searchForImages() {
        viewModelScope.launchWithErrorHandling {
            getImagesWithPagingUseCase(_input.value.query).cachedIn(viewModelScope)
                .collect {
                    _pagingData.value = it
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