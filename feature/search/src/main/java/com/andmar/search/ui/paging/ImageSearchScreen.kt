package com.andmar.search.ui.paging

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.andmar.common.navigation.AppNavigator
import com.andmar.search.R
import com.andmar.search.ui.ImageItemProvider
import com.andmar.search.ui.SearchScreenInput
import com.andmar.search.ui.components.SearchBar
import com.andmar.ui.ObserveAsEvents
import com.ramcosta.composedestinations.annotation.Destination
import timber.log.Timber

@Destination
@Composable
internal fun ImageSearchPagingScreen(
    appNavigator: AppNavigator,
    snackbarHostState: SnackbarHostState,
    viewModel: ImageSearchPagingViewModel = hiltViewModel(),
) {
    val input = viewModel.input.collectAsState().value

    val imagesResult = viewModel.pagingData.collectAsLazyPagingItems()
    ImageSearchMainContent(
        input = input,
        imagesResult = imagesResult,
        snackbarHostState = snackbarHostState,
        onNewQuery = viewModel::onNewQuery,
        onSearch = viewModel::searchForImages,
        reloadData = viewModel::searchForImages,
        onImageClick = { image ->
            viewModel.onImageClick(image)
        }
    )

    if (input.showDialog.isPresent) {
        ConfirmDetailsOpenDialog(
            input.showDialog.get(),
            viewModel::onConfirmImageOpen,
            viewModel::onDismissImageOpenDialog
        )
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ImageSearchAction.OpenImageDetails -> {
                appNavigator.navigateToImageDetails(event.image.id)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageSearchMainContent(
    input: SearchScreenInput,
    imagesResult: LazyPagingItems<ImageItem>,
    snackbarHostState: SnackbarHostState,
    onNewQuery: (String) -> Unit,
    onSearch: () -> Unit,
    reloadData: () -> Unit,
    onImageClick: (ImageItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier.zIndex(2f),
            query = input.query,
            onQueryChange = onNewQuery,
            onSearch = onSearch
        )
        val state = rememberPullToRefreshState()
        if (imagesResult.loadState.refresh is LoadState.NotLoading) {
            LaunchedEffect(imagesResult.loadState) {
                state.endRefresh()
            }
        }
        if (state.isRefreshing) {
            LaunchedEffect(state.isRefreshing) {
                reloadData()
            }
        }
        val lazyStaggeredGridState = rememberLazyStaggeredGridState()

        LaunchedEffect(imagesResult.itemCount) {
            Timber.d("ImageSearchMainContent: imagesResult.loadState.refresh=${imagesResult.loadState.refresh}")
            Timber.d("ImageSearchMainContent size: ${imagesResult.itemCount}")
        }

        Box(
            Modifier
                .fillMaxSize()
                .nestedScroll(state.nestedScrollConnection)
        ) {
            if (imagesResult.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (imagesResult.loadState.refresh is LoadState.Error && imagesResult.itemCount == 0) {
                ImageSearchErrorState(onReload = { imagesResult.retry() })
            } else {
                if (imagesResult.loadState.refresh is LoadState.Error) {
                    val errorMessage =
                        stringResource(R.string.search_images_error_on_list_with_cached_data_loaded)
                    LaunchedEffect(imagesResult.loadState.refresh) {
                        snackbarHostState.showSnackbar(errorMessage)
                    }
                }
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    state = lazyStaggeredGridState,
                    content = {
                        items(
                            imagesResult.itemCount,
                            key = imagesResult.itemKey { it.id }) { image ->
                            imagesResult[image]?.let {
                                ImageGridItem(
                                    image = it,
                                    onImageClick = onImageClick
                                )
                            }
                        }
                        item(span = StaggeredGridItemSpan.FullLine) {
                            if (imagesResult.loadState.append is LoadState.Loading) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(16.dp)
                                    )
                                }
                            } else if (imagesResult.loadState.append is LoadState.Error) {
                                ImageSearchErrorState(onReload = { imagesResult.retry() })
                            }
                        }
                    })
            }
            PullToRefreshContainer(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(0.1f),
                state = state,
            )

        }
    }

}


@Composable
private fun ImageSearchErrorState(onReload: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "An error occurred",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Please try again",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Button(onClick = onReload) {
            Text(
                text = "Reload",
                style = TextStyle(fontWeight = FontWeight.Bold),
            )
        }
    }
}


@Composable
@Preview
private fun ImageGridItem(
    @PreviewParameter(ImageItemProvider::class) image: ImageItem,
    onImageClick: (ImageItem) -> Unit = { },
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { onImageClick(image) })
    ) {
        val textColor = remember { mutableStateOf(Color.Black) }

        AsyncImage(
            model = image.thumbSource.url,
            contentDescription = stringResource(R.string.image_details_item_image_content_description),
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(image.thumbSource.width.toFloat() / image.thumbSource.height),
            contentScale = ContentScale.FillWidth,

            )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.White.copy(alpha = 0.8f), shape = RectangleShape)
                .padding(8.dp)
        ) {
            Text(
                text = image.username,
                style = TextStyle(fontWeight = FontWeight.Bold, color = textColor.value)
            )
            Text(
                text = image.tags.joinToString(", "),
                style = TextStyle(color = textColor.value)
            )
        }
    }

}

