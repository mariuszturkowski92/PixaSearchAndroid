package com.andmar.pixabaysearch.navigation

import androidx.navigation.NavController
import com.andmar.common.navigation.AppNavigator
import com.andmar.feature.details.destinations.ImageDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.navigate

class AppNavigatorImpl(private val navController: NavController) : AppNavigator {
    override fun navigateToImageDetails(imageId: Int) {
        navController.navigate(ImageDetailsScreenDestination(imageId))
    }
}