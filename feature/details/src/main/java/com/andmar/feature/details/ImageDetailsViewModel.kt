package com.andmar.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.andmar.common.utils.result.Result
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.usecase.GetImageWithIdUseCase
import com.andmar.ui.state.ErrorData
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
        mutableUiState.launchWithErrorHandlingOn(
            coroutineScope = viewModelScope,
            showLoading = true
        ) {
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
        mutableUiState.launchWithErrorHandlingOn(viewModelScope, retryTag = GET_IMAGE_RETRY_TAG) {
            getImageWithId(imageId, forceReload).collect { result ->
                when (result) {
                    is Result.Failure -> {
                        showError(ErrorData.Error(result.error, GET_IMAGE_RETRY_TAG))
                    }
                    is Result.Success -> {
                        mutableUiState.success(result.data)
                    }
                }
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