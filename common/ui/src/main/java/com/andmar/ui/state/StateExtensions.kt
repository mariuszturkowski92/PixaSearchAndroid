package com.andmar.ui.state

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.andmar.ui.R


@Composable
fun <T> UiState<T>.StateHandling(
    retryHandler: RetryHandler<ErrorData>?,
    content: @Composable (T) -> Unit = {},
    composableBuilder: StateComposableBuilder<T, ErrorData> = remember {
        StateComposableBuilder(
            retryHandler,
            content = content
        )
    },
    onEmpty: @Composable (composableBuilder: StateComposableBuilder<T, ErrorData>) -> Unit = {
        composableBuilder.empty()
    },
    onLoading: @Composable (T?, composableBuilder: StateComposableBuilder<T, ErrorData>) -> Unit = { data, cb ->
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            if (data != null) {
                cb.content(data)
            }
            cb.loader(data)
        }
    },
    /**
     * Called when an error is received. If this returns true, the error will be considered handled and will not be shown
     * @return true if the error was handled, false otherwise
     */
    onError: @Composable ((UiState<T>, composableBuilder: StateComposableBuilder<T, ErrorData>) -> Boolean) = { state, cb ->
        state.data?.let {
            cb.content(it)
        }
        false
    },
) {
    when (state) {
        is StateType.Error -> {
            if (!onError.invoke(this, composableBuilder))
                composableBuilder.error(this)
        }

        StateType.Loading -> {
            onLoading.invoke(data, composableBuilder)
        }

        StateType.Empty -> {
            onEmpty.invoke(composableBuilder)
        }

        StateType.None -> {
            // Do nothing
        }

        StateType.Success -> {
            composableBuilder.content(data!!)
        }
    }

}

open class StateComposableBuilder<T, ED : ErrorData>(
    retryHandler: RetryHandler<ED>?,
    val content: @Composable (T) -> Unit,
    val loader: @Composable (T?) -> Unit = {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    },
    val empty: @Composable () -> Unit = {},
    val error: @Composable (UiState<T>) -> Unit = { state ->
        AlertDialog(
            onDismissRequest = {
                retryHandler?.onDismissErrorDialog()
            },
            title = {
                Text(
                    text = stringResource(id = R.string.error_default_title)
                )
            },
            text = {
                Text(
                    modifier = Modifier.scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical
                    ),
                    text = (state.state as StateType.Error).error?.message
                        ?: stringResource(id = R.string.error_default_message)
                )
            },
            confirmButton = {
                Button(onClick = {
                    retryHandler?.retry((state.state as? StateType.Error)?.retryTag)
                }) {
                    Text(
                        text = stringResource(id = R.string.error_default_retry_button)
                    )
                }
            },
            dismissButton = {
                Button(onClick = {
                    retryHandler?.onCloseErrorClicked()
                }) {
                    Text(
                        text = stringResource(id = R.string.error_default_close_button)
                    )
                }
            }

        )

    },
)


