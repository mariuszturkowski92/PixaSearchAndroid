package com.andmar.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

open class BasicRetryHandler<T>(val provideUIStateMutableFlow: () -> MutableStateFlow<UiState<T>>) :
    RetryHandler {


    override fun retry(tag: String?) {
        Timber.d("retry")
    }

    override fun onCloseErrorClicked(): Boolean {
        Timber.d("onCloseErrorClicked")
        (provideUIStateMutableFlow().value.state as? StateType.Error)?.let {
            updateToStateBeforeError(it.stateTypeBeforeError)
        }
        return false
    }

    override fun <T> onDismissErrorDialog(state: UiState<T>?) {
        Timber.d("onDismissErrorDialog")
        (state?.state as? StateType.Error)?.let {
            updateToStateBeforeError(it.stateTypeBeforeError)
        }
    }

    open fun updateToStateBeforeError(stateType: StateType) {
        provideUIStateMutableFlow().update {
            it.copy(state = stateType)
        }
    }
}