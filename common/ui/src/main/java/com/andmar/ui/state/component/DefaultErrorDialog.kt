package com.andmar.ui.state.component

import android.content.Context
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.andmar.ui.state.ErrorData
import com.andmar.ui.state.RetryHandler

@Composable
fun <ED : ErrorData> DefaultErrorDialog(errorData: ED, retryHandler: RetryHandler<ED>) {
    AlertDialog(
        onDismissRequest = {
            retryHandler.onDismissErrorDialog()
        },
        title = {
            Text(
                text = stringResource(id = com.andmar.ui.R.string.error_default_title)
            )
        },
        text = {
            Text(
                modifier = Modifier.scrollable(
                    state = rememberScrollState(),
                    orientation = Orientation.Vertical
                ),
                text = errorData.getErrorMessage(LocalContext.current)
            )
        },
        confirmButton = {
            Button(onClick = {
                retryHandler.retry(errorData.errorTag)
            }) {
                Text(
                    text = stringResource(id = com.andmar.ui.R.string.error_default_retry_button)
                )
            }
        },
        dismissButton = {
            Button(onClick = {
                retryHandler.onCloseErrorClicked()
            }) {
                Text(
                    text = stringResource(id = com.andmar.ui.R.string.error_default_close_button)
                )
            }
        }

    )
}

private fun ErrorData.getErrorMessage(current: Context): String {
    return when (this) {
        is ErrorData.ResourceError -> getErrorMessage(current)
        is ErrorData.StringError -> getErrorMessage(current)
        is ErrorData.ThrowableError -> getErrorMessage(current)
        is ErrorData.Error<*> -> "Error"
    }
}
