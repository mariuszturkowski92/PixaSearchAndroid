package com.andmar.search.ui.paging

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.andmar.data.images.entity.ImagesResult
import com.andmar.data.images.entity.PSImage
import com.andmar.search.ui.PSImageProvider
import com.andmar.search.ui.SearchScreenInput
import com.andmar.search.ui.components.SearchBar
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
internal fun ImageSearchPagingScreen(
    navController: NavController,
    viewModel: ImageSearchPagingViewModel = hiltViewModel(),
) {
    val input = viewModel.input.collectAsState().value

    val imagesResult = viewModel.pagingData.collectAsLazyPagingItems()
    ImageSearchMainContent(
        input = input,
        onNewQuery = viewModel::onNewQuery,
        onSearch = viewModel::searchForImages,
        reloadData = viewModel::searchForImages,
        imagesResult = imagesResult,
    )

//    val uiState = viewModel.uiState.collectAsState().value
//    uiState.StateHandling(
//        retryHandler = viewModel,
//        content = { imagesResult ->
//            ImageSearchMainContent(
//                input = input,
//                onNewQuery = viewModel::onNewQuery,
//                onSearch = viewModel::searchForImages,
//                block = {
//                    ImageSearchList(
//                        imagesResult = imagesResult,
//                        onImageClick = { image ->
//                            //TODO
//                            navController.navigate("image_detail_screen/${image.id}")
//                        }
//                    )
//                })
//        },
//        onError = { state, composableBuilder ->
//            if (state.data != null) {
//                composableBuilder.content(state.data!!)
//                false
//            } else {
//                val stateType = state.state as com.andmar.ui.state.StateType.Error
//                ImageSearchMainContent(
//                    input = input,
//                    onNewQuery = viewModel::onNewQuery,
//                    onSearch = viewModel::searchForImages,
//                    block = {
//                        ImageSearchErrorState(
//                            onReload = { viewModel.retry(stateType.retryTag) }
//                        )
//                    })
//                true
//            }
//        },
//        onEmpty = {
//            it.empty()
//        },
//    )

}

@Composable
fun ImageSearchErrorState(onReload: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "An error occurred",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Please try again",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Reload",
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.clickable(onClick = onReload)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageSearchMainContent(
    input: SearchScreenInput,
    imagesResult: LazyPagingItems<PSImage>,
    onNewQuery: (String) -> Unit,
    onSearch: () -> Unit,
    reloadData: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = input.query,
            onQueryChange = onNewQuery,
            onSearch = onSearch
        )
        val state = rememberPullToRefreshState()
        if (state.isRefreshing) {
            LaunchedEffect(state.isRefreshing) {
                reloadData()
            }
        }
        if (imagesResult.loadState.refresh is LoadState.NotLoading) {
            LaunchedEffect(imagesResult.loadState) {
                state.endRefresh()
            }
        }
        Box(Modifier.nestedScroll(state.nestedScrollConnection)) {
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(160.dp), content = {
                items(imagesResult.itemCount) { image ->
                    imagesResult[image]?.let { ImageItem(image = it, onImageClick = { }) }
                }

            })
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = state,
            )
        }
    }

}

@Composable
private fun ColumnScope.ImageSearchList(
    imagesResult: ImagesResult,
    onImageClick: (PSImage) -> Unit,
) {
    ImageList(modifier = Modifier.fillMaxWidth(), images = imagesResult.images)
}

@Composable
fun ImageList(images: List<PSImage>, modifier: Modifier = Modifier, onImageClick: (PSImage) -> Unit = { }) {
    LazyVerticalStaggeredGrid(modifier = modifier, columns = StaggeredGridCells.Fixed(2), content = {
        items(images) { image ->
            ImageItem(image, onImageClick)
        }
    })
}


@Composable
@Preview
fun ImageItem(
    @PreviewParameter(PSImageProvider::class) image: PSImage,
    onImageClick: (PSImage) -> Unit = { },
) {

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Box(modifier = Modifier.clickable(onClick = { onImageClick(image) })) {
            val textColor = remember { mutableStateOf(Color.Black) }

            AsyncImage(
                model = image.thumbSource.url,
                contentDescription = "Image thumbnail",
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(image.thumbSource.width.toFloat() / image.thumbSource.height),
                contentScale = ContentScale.FillWidth,
                onSuccess = { success ->
                    val drawable = success.result.drawable
//                    val palette = drawable.toBitmapOrNull(config = Bitmap.Config.ARGB_8888)?.let { Palette.from(it).generate() }
//                    textColor.value = when {
//                        palette?.darkVibrantSwatch != null -> Color.White
//                        else -> Color.Black
//                    }
                },
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
}

