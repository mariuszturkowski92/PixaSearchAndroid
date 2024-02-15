package com.andmar.feature.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.andmar.data.images.entity.PSImage
import com.andmar.ui.state.StateHandling
import com.andmar.ui.state.StateType
import com.ramcosta.composedestinations.annotation.Destination

// create destination
@Destination
@Composable
fun ImageDetailsScreen(
    imageId: Int,
    viewModel: ImageDetailViewModel = hiltViewModel(),
) {
    viewModel.uiState.collectAsState().value.StateHandling(
        retryHandler = viewModel,
        content = { ImageDetailContent(it) },
        onError = { state, cb ->
            state.data?.let {// if data is not null, show content and default error handling
                cb.content(it)
                false
            } ?: run {
                val errorState = state.state as StateType.Error
                ImageDetailsError(state.state as StateType.Error) { viewModel.retry(errorState.retryTag) }
                true
            }
        }
    )
}

@Composable
private fun ImageDetailsError(error: StateType.Error, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.image_details_full_screen_error_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = error.error?.localizedMessage
                ?: stringResource(R.string.image_details_full_screen_error_default_message),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.image_details_full_screen_error_retry_button))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ImageDetailContent(imageDetail: PSImage) {
    val aspectRatio = imageDetail.largeImage.width.toFloat() / imageDetail.largeImage.height.toFloat()

    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = imageDetail.largeImage.url,
            contentDescription = "Image",
            placeholder = if (LocalInspectionMode.current) {
                BrushPainter(
                    Brush.linearGradient(
                        listOf(
                            Color(color = 0xFFFFFFFF),
                            Color(color = 0xFFDDDDDD),
                        )
                    )
                )
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        )
        Text(
            modifier = Modifier.align(Alignment.End),
            text = "Created by: ${imageDetail.username}",
            style = MaterialTheme.typography.bodySmall
        )
        FlowRow(modifier = Modifier.padding(horizontal = 8.dp)) {
            imageDetail.tags.forEach { tag ->
                SuggestionChip(onClick = {}, label = { Text(text = tag) })
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconTextColumn(
                painter = rememberVectorPainter(Icons.Filled.FavoriteBorder),
                text = "${imageDetail.likes}",
                contentDescription = "Likes"
            )
            IconTextColumn(
                painter = painterResource(R.drawable.outline_download_24),
                text = "${imageDetail.downloads}",
                contentDescription = "Downloads"
            )

            IconTextColumn(
                painter = painterResource(R.drawable.outline_comment_24),
                text = "${imageDetail.comments}",
                contentDescription = "Comments",
            )

        }

    }
}

@Composable
private fun IconTextColumn(painter: Painter, text: String, contentDescription: String) {
    Column(
        modifier = Modifier.padding(end = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter, contentDescription = contentDescription)
        Text(text = text, style = MaterialTheme.typography.headlineSmall)
    }
}

//create a preview
@SuppressLint("VisibleForTests")
@Preview
@Composable
private fun ImageDetailContentPreview() {
    ImageDetailContent(imageDetail = PSImage.testModel(1))
}