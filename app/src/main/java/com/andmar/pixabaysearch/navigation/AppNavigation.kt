package com.andmar.pixabaysearch.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.andmar.pixabaysearch.navigation.AppNavigatorImpl
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.app.navgraphs.MainNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
) {
    //pass your enter/exit transitions here
    val appNavigator = remember(navController) { AppNavigatorImpl(navController) }

    val navHostEngine = rememberNavHostEngine()

    DestinationsNavHost(
        engine = navHostEngine,
        navController = navController,
        navGraph = MainNavGraph,
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(snackbarHostState)
            dependency(appNavigator)
        }
    )
}