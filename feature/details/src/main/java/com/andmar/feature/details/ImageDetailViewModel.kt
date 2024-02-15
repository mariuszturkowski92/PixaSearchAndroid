package com.andmar.feature.details

import androidx.lifecycle.SavedStateHandle
import com.andmar.data.images.entity.PSImage
import com.andmar.ui.state.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    val handle: SavedStateHandle,
    val getImageWithId: GetImageWithIdUseCase,
) : StateViewModel<PSImage>() {

    //TODO change into the flow implementation
    val imageId: Int = handle.get("imageId") ?: throw IllegalArgumentException("Image ID is missing")

}