package com.andmar.search.ui.paging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.andmar.data.images.usecase.GetImagesWithPagingUseCase
import com.andmar.search.ui.ImageItem
import com.andmar.search.ui.SearchScreenInput
import com.andmar.ui.state.launchWithErrorHandling
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.util.Optional
import javax.inject.Inject

@HiltViewModel
internal class ImageSearchPagingViewModel @Inject constructor(
    val handle: SavedStateHandle,
    private val getImagesWithPagingUseCase: GetImagesWithPagingUseCase,
) : ViewModel() {

    private val _input = MutableStateFlow(SearchScreenInput(DEFAULT_SEARCH_QUERY))
    val input: StateFlow<SearchScreenInput>
        get() = _input.asStateFlow()

    private val _pagingData: MutableStateFlow<PagingData<ImageItem>> = MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<ImageItem>>
        get() = _pagingData.asStateFlow()

    private val _events = Channel<ImageSearchAction>()
    val events: Flow<ImageSearchAction>
        get() = _events.receiveAsFlow()

    init {
        searchForImages()
    }

    fun searchForImages() {
        viewModelScope.launchWithErrorHandling {
            getImagesWithPagingUseCase(_input.value.query).cachedIn(viewModelScope)
                .collect {
                    _pagingData.value = it.map { image ->
                        ImageItem.fromPSImage(image)
                    }
                }
        }
    }

    fun onNewQuery(newQuery: String) {
        _input.value = _input.value.copy(query = newQuery)
    }

    fun onImageClick(image: ImageItem) {
        _input.update {
            it.copy(showDialog = Optional.of(image))
        }
    }

    fun onConfirmImageOpen(image: ImageItem) {
        viewModelScope.launchWithErrorHandling {
            _events.send(ImageSearchAction.OpenImageDetails(image))
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
    data class OpenImageDetails(val image: ImageItem) : ImageSearchAction()
}
