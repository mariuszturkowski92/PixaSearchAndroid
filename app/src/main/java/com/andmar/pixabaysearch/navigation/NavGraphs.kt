package com.andmar.pixabaysearch.navigation


import com.ramcosta.composedestinations.annotation.ExternalNavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.generated.details.navgraphs.DetailsGraph
import com.ramcosta.composedestinations.generated.search.navgraphs.SearchGraph


@NavHostGraph(defaultTransitions = RootAnimationStyle::class)
annotation class MainGraph {
    @ExternalNavGraph<DetailsGraph>
    @ExternalNavGraph<SearchGraph>(start = true)
    companion object Includes
}

//object NavGraphs {
//
//    //stat's module NavGraph
//    val images = object : NavGraphSpec {
//        override val route = "images"
//        override val startRoute = ImageSearchPagingScreenDestination
//        override val destinationsByRoute = searchDestinations
//            .associateBy { it.route }
//    }
//
//    val details = object : NavGraphSpec {
//        override val route = "details"
//        override val startRoute = ImageDetailsScreenDestination
//        override val destinationsByRoute = detailsDestinations
//            .associateBy { it.route }
//    }
//
//    //Root NavGraph showing Overview as the starting screen
//
//    val root = object : NavGraphSpec {
//        override val route = "root"
//        override val startRoute = images
//        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
//        override val nestedNavGraphs = listOf(images, details)
//    }

//}