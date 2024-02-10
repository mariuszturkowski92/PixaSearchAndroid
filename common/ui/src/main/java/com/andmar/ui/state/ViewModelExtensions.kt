package com.andmar.ui.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchWithErrorHandling(
    context: CoroutineContext = EmptyCoroutineContext,
    onError: (Throwable) -> Unit = {
        Timber.e("Error in launchWithErrorHandling", it)
    },
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return this.launch {
        try {
            block()
        } catch (e: Exception) {
            onError(e)
        }
    }
}

fun <T> MutableStateFlow<State<T>>.launchWithErrorHandlingOn(
    coroutineScope: CoroutineScope,
    showLoading: Boolean = true,
    retryTag: String? = null,
    errorDialogType: ErrorDialogType = ErrorDialogType.CLOSE,
    onError: (Throwable) -> Unit = {
        Timber.e(t = it, message = it.message ?: "Error in launchWithErrorHandlingOn")
        this.error(it, retryTag, errorDialogType)
    },
    block: suspend CoroutineScope.() -> Unit,
) = coroutineScope.launchWithErrorHandling(onError = onError) {
    if (showLoading)
        loading()
    block()

}