package com.andmar.ui.state

import kotlinx.coroutines.flow.MutableStateFlow

data class State<T>(
    val data: T? = null,
    val state: StateType = StateType.None,
) {
    companion object {
        fun <T> none(): State<T> = State()
    }

    fun loading(): State<T> = copy(state = StateType.Loading)
    fun success(): State<T> = copy(state = StateType.Success)
    fun success(data: T): State<T> = copy(data = data, state = StateType.Success)
    fun error(throwable: Throwable, retryTag: String?, errorDialogType: ErrorDialogType): State<T> =
        copy(
            state = StateType.Error(
                throwable,
                retryTag,
                errorDialogType,
                stateTypeBeforeError = if (state is StateType.Error) state.stateTypeBeforeError else state
            )
        )

    fun none(): State<T> = copy(state = StateType.None)
}

fun <T> MutableStateFlow<State<T>>.loading() {
    this.value = value.loading()
}

fun <T> MutableStateFlow<State<T>>.success(data: T) {
    this.value = value.success(data)
}

fun <T> MutableStateFlow<State<T>>.error(
    throwable: Throwable,
    retryTag: String?,
    errorDialogType: ErrorDialogType,
) {
    this.value = value.error(throwable, retryTag, errorDialogType)
}

fun <T> MutableStateFlow<State<T>>.none() {
    this.value = value.none()
}

sealed class StateType {
    data object None : StateType()
    data object Loading : StateType()
    data object Success : StateType()
    data object Empty : StateType()
    data class Error(
        val error: Throwable?,
        val retryTag: String?,
        val errorDialogType: ErrorDialogType,
        val stateTypeBeforeError: StateType = None,
    ) : StateType()
}