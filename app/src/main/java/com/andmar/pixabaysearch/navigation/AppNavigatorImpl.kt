package com.andmar.pixabaysearch.navigation

import androidx.navigation.NavController
import com.andmar.common.navigation.AppNavigator
import com.ramcosta.composedestinations.generated.details.destinations.ImageDetailsScreenDestination

class AppNavigatorImpl(private val navController: NavController) : AppNavigator {
    override fun navigateToImageDetails(imageId: Int) {
        navController.navigate(ImageDetailsScreenDestination(imageId).route)
    }
}