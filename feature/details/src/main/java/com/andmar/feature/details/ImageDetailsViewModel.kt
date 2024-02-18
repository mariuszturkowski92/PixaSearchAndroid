package com.andmar.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.usecase.GetImageWithIdUseCase
import com.andmar.ui.state.StateViewModel
import com.andmar.ui.state.launchWithErrorHandlingOn
import com.andmar.ui.state.success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ImageDetailsViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val getImageWithId: GetImageWithIdUseCase,
) : StateViewModel<PSImage>() {


    private val imageId = handle.getStateFlow("imageId", Integer.MIN_VALUE)

    init {
        _uiState.launchWithErrorHandlingOn(coroutineScope = viewModelScope, showLoading = true) {
            imageId.stateIn(this).collect { id ->
                if (id != Integer.MIN_VALUE) {
                    getImageById(id)
                }
            }
        }
    }

    fun refresh() {
        getImageById(imageId.value, true)
    }

    private fun getImageById(imageId: Int, forceReload: Boolean = false) {
        _uiState.launchWithErrorHandlingOn(viewModelScope, retryTag = GET_IMAGE_RETRY_TAG) {
            getImageWithId(imageId, forceReload).collect { image ->
                _uiState.success(image)
            }
        }
    }

    override fun retry(tag: String?) {
        if (tag == GET_IMAGE_RETRY_TAG) {
            refresh()
        }
    }

    companion object {
        const val GET_IMAGE_RETRY_TAG = "GET_IMAGE_RETRY_TAG"
    }
}