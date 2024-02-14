package com.andmar.pixabaysearch.navigation


import com.andmar.search.ui.paging.destinations.ImageSearchPagingScreenDestination
import com.andmar.search.ui.paging.searchDestinations
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec


object NavGraphs {

    //stat's module NavGraph
    val images = object : NavGraphSpec {
        override val route = "images"
        override val startRoute = ImageSearchPagingScreenDestination
        override val destinationsByRoute = searchDestinations
            .associateBy { it.route }
    }

    //Root NavGraph showing Overview as the starting screen

    val root = object : NavGraphSpec {
        override val route = "root"
        override val startRoute = images
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(images)
    }

}