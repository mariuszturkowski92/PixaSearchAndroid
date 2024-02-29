package com.andmar.ui.state

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow

@Stable
data class UiState<T>(
    val data: T? = null,
    val state: StateType = StateType.None,
) {
    companion object {
        fun <T> none(): UiState<T> = UiState()
    }

    fun loading(): UiState<T> = copy(state = StateType.Loading)
    fun success(): UiState<T> = copy(state = StateType.Success)
    fun success(data: T): UiState<T> = copy(data = data, state = StateType.Success)
    fun error(throwable: Throwable, retryTag: String?, errorDialogType: ErrorDialogType): UiState<T> =
        copy(
            state = StateType.Error(
                throwable,
                retryTag,
                errorDialogType,
                stateTypeBeforeError = if (state is StateType.Error) state.stateTypeBeforeError else state
            )
        )

    fun none(): UiState<T> = copy(state = StateType.None)
}

fun <T> MutableStateFlow<UiState<T>>.loading() {
    this.value = value.loading()
}

fun <T> MutableStateFlow<UiState<T>>.success(data: T) {
    this.value = value.success(data)
}

fun <T> MutableStateFlow<UiState<T>>.error(
    throwable: Throwable,
    retryTag: String?,
    errorDialogType: ErrorDialogType,
) {
    this.value = value.error(throwable, retryTag, errorDialogType)
}

fun <T> MutableStateFlow<UiState<T>>.none() {
    this.value = value.none()
}
@Stable
sealed class StateType {
    data object None : StateType()
    data object Loading : StateType()
    data object Success : StateType()
    data object Empty : StateType()
    @Stable
    data class Error(
        val error: Throwable?,
        val retryTag: String?,
        val errorDialogType: ErrorDialogType,
        val stateTypeBeforeError: StateType = None,
    ) : StateType()
}