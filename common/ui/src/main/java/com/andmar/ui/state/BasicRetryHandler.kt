package com.andmar.ui.state

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber
import java.util.Optional

open class BasicRetryHandler<ED : ErrorData>() :
    RetryHandler<ED> {

    private val _errorData = Channel<Optional<ED>>()
    override val errorData: Flow<Optional<ED>>
        get() = _errorData.receiveAsFlow()

    override fun showError(errorData: ED) {
        Timber.d("showError in viewModel")
        _errorData.trySend(Optional.of(errorData))
    }


    override fun retry(tag: String?) {
        Timber.d("retry in viewModel")
    }

    override fun onCloseErrorClicked(): Boolean {
        Timber.d("onCloseErrorClicked")
        _errorData.trySend(Optional.empty())
        return false
    }

    override fun onDismissErrorDialog() {
        _errorData.trySend(Optional.empty())
    }


}