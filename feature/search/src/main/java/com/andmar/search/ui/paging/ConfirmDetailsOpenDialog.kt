package com.andmar.search.ui.paging

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.andmar.search.R
import com.andmar.search.ui.ImageItemProvider

@Composable
@Preview
internal fun ConfirmDetailsOpenDialog(
    @PreviewParameter(ImageItemProvider::class) image: ImageItem,
    onConfirmImageOpen: (ImageItem) -> Unit = {},
    onDismissImageOpenDialog: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismissImageOpenDialog,
        confirmButton = {
            Button(onClick = {
                onConfirmImageOpen(image)
            }) {
                Text(text = stringResource(R.string.image_search_confirm_dialog_confirm_button))
            }
        },
        dismissButton = {
            Button(onClick = onDismissImageOpenDialog) {
                Text(text = stringResource(R.string.image_search_confirm_dialog_dismiss_button))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.image_search_confirm_dialog_title),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.image_search_confirm_dialog_message),
                style = MaterialTheme.typography.bodyMedium
            )
        },
    )
}