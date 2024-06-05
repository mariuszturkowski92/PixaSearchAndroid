package com.andmar.ui.state

import android.content.Context

sealed class ErrorData(
    open val errorTag: String? = null,
    open val errorDialogType: ErrorDialogType? = null
) {

    data class ThrowableError(
        val throwable: Throwable,
        override val errorTag: String? = null,
        override val errorDialogType: ErrorDialogType? = null,
    ) : ErrorData(errorTag, errorDialogType) {
    }

    data class StringError(
        val message: String,
        override val errorTag: String? = null,
        override val errorDialogType: ErrorDialogType? = null,
    ) : ErrorData(errorTag, errorDialogType) {
    }

    data class ResourceError(
        val resourceId: Int,
        override val errorTag: String? = null,
        override val errorDialogType: ErrorDialogType? = null,
    ) : ErrorData(errorTag, errorDialogType) {
    }


    data class Error<out E : com.andmar.common.utils.error.Error>(
        val error: E, override val errorTag: String? = null,
        override val errorDialogType: ErrorDialogType? = null,
    ) : ErrorData(errorTag, errorDialogType)
}

fun ErrorData.ResourceError.getErrorMessage(context: Context): String =
    context.getString(resourceId)

fun ErrorData.StringError.getErrorMessage(context: Context): String = message

fun ErrorData.ThrowableError.getErrorMessage(context: Context): String {
    return throwable.message ?: ""
}