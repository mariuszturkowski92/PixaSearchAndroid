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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.Optional
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

    private val _events = MutableSharedFlow<ImageSearchAction>()
    val events: SharedFlow<ImageSearchAction>
        get() = _events.asSharedFlow()

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

    fun onImageClick(image: PSImage) {
        _input.value = _input.value.copy(showDialog = Optional.of(image))
    }

    fun onConfirmImageOpen(image: PSImage) {
        viewModelScope.launchWithErrorHandling {
            _events.emit(ImageSearchAction.OpenImageDetails(image))
            _input.value = _input.value.copy(showDialog = Optional.empty())
        }
    }

    fun onDismissImageOpenDialog() {
        _input.value = _input.value.copy(showDialog = Optional.empty())
    }

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "fruits"
    }


}

internal sealed class ImageSearchAction {
    data class OpenImageDetails(val image: PSImage) : ImageSearchAction()
}
