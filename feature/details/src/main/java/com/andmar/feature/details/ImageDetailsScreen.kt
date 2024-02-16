package com.andmar.feature.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
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
import com.andmar.ui.state.UiState
import com.ramcosta.composedestinations.annotation.Destination

// create destination
@Destination
@Composable
fun ImageDetailsScreen(
    imageId: Int,
    viewModel: ImageDetailsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    uiState.value.StateHandling(
        retryHandler = viewModel,
        content = { ImageDetailContent(uiState.value, onRefresh = viewModel::refresh) },
        onError = { state, cb ->
            state.data?.let {// if data is not null, show content and default error handling
                cb.content(it)
                false
            } ?: run {
                val errorState = state.state as StateType.Error
                ImageDetailsError(state.state as StateType.Error) { viewModel.retry(errorState.retryTag) }
                true
            }
        },
        onLoading = { data, cb ->
            if (data != null) {
                cb.content(data) // do not show special loading screen if when there is a data already, it will be shown as pull to refresh indicator in content
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    cb.loader(data)
                }
            }
        }
    )
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ImageDetailContent(uiState: UiState<PSImage>, onRefresh: () -> Unit = {}) {
    val imageDetails = uiState.data ?: return
    val aspectRatio = imageDetails.largeImage.width.toFloat() / imageDetails.largeImage.height.toFloat()

    val state = rememberPullToRefreshState()
    if (uiState.state != StateType.Loading) {
        LaunchedEffect(uiState.state) {
            state.endRefresh()
        }
    }
    if (state.isRefreshing) {
        LaunchedEffect(state.isRefreshing) {
            onRefresh()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
                .padding(bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp)
        ) {
            AsyncImage(
                model = imageDetails.largeImage.url,
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
                text = stringResource(R.string.details_craeated_by_label, imageDetails.username),
                style = MaterialTheme.typography.bodySmall
            )
            FlowRow(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = stringResource(R.string.details_screen_tags_label),
                    style = MaterialTheme.typography.labelMedium
                )
                imageDetails.tags.forEach { tag ->
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        tonalElevation = 2.dp,
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            text = tag,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                IconTextColumn(
                    painter = rememberVectorPainter(Icons.Filled.FavoriteBorder),
                    text = "${imageDetails.likes}",
                    contentDescription = "Likes"
                )
                IconTextColumn(
                    painter = painterResource(R.drawable.outline_download_24),
                    text = "${imageDetails.downloads}",
                    contentDescription = "Downloads"
                )

                IconTextColumn(
                    painter = painterResource(R.drawable.outline_comment_24),
                    text = "${imageDetails.comments}",
                    contentDescription = "Comments",
                )

            }

        }
        PullToRefreshContainer(
            modifier = Modifier
                .align(Alignment.TopCenter),
            state = state,
        )

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

@SuppressLint("VisibleForTests")
@Preview
@Composable
private fun ImageDetailContentPreview() {
    ImageDetailContent(uiState = UiState(PSImage.testModel(1), StateType.Success))
}