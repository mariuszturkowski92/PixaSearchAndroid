package com.andmar.ui.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateViewModel<T>() : ViewModel(), RetryHandler<ErrorData> by BasicRetryHandler<ErrorData>() {

    protected val mutableUiState = MutableStateFlow(UiState.none<T>())

    val uiState: StateFlow<UiState<T>>
        get() = mutableUiState.asStateFlow()

}
