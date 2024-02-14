package com.andmar.ui.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

abstract class StateViewModel<T> : ViewModel(), RetryHandler {

    protected val _uiState: MutableStateFlow<State<T>> = MutableStateFlow(State.none())
    val uiState: MutableStateFlow<State<T>>
        get() = _uiState

    override fun retry(tag: String?) {
        Timber.d("retry")
    }

    override fun onCloseErrorClicked(): Boolean {
        Timber.d("onCloseErrorClicked")
        (_uiState.value.state as? StateType.Error)?.let {
            _uiState.value = _uiState.value.copy(state = it.stateTypeBeforeError)
        }
        return false
    }

    override fun <T> onDismissErrorDialog(state: State<T>?) {
        Timber.d("onDismissErrorDialog")
        (_uiState.value.state as? StateType.Error)?.let {
            _uiState.value = _uiState.value.copy(state = it.stateTypeBeforeError)
        }
    }
}
