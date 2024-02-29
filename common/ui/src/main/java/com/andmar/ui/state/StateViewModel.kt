package com.andmar.ui.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateViewModel<T>(
    protected val mutableUiState: MutableStateFlow<UiState<T>> = MutableStateFlow(
        UiState.none()
    ),
) : ViewModel(), RetryHandler by BasicRetryHandler({ mutableUiState }) {
    val uiState: StateFlow<UiState<T>>
        get() = mutableUiState.asStateFlow()


}
