package com.andmar.ui.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.launchWithErrorHandling(
    context: CoroutineContext = EmptyCoroutineContext,
    onError: (Throwable) -> Unit = {
        Timber.e("Error in launchWithErrorHandling", it)
    },
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return this.launch(context) {
        try {
            block()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            onError(e)
        }
    }
}

fun <T> MutableStateFlow<UiState<T>>.launchWithErrorHandlingOn(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
    showLoading: Boolean = true,
    retryTag: String? = null,
    errorDialogType: ErrorDialogType = ErrorDialogType.CLOSE,
    onError: (Throwable) -> Unit = {
        Timber.e(t = it, message = it.message ?: "Error in launchWithErrorHandlingOn")
        this.error(it, retryTag, errorDialogType)
    },
    block: suspend CoroutineScope.() -> Unit,
) = coroutineScope.launchWithErrorHandling(context = context, onError = onError) {
    if (showLoading)
        loading()
    block()

}