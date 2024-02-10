package com.andmar.pixabaysearch.navigation


import com.andmar.search.ui.destinations.ImageSearchScreenDestination
import com.andmar.search.ui.searchDestinations
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec


object NavGraphs {

    //stat's module NavGraph
    val images = object : NavGraphSpec {
        override val route = "images"
        override val startRoute = ImageSearchScreenDestination
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