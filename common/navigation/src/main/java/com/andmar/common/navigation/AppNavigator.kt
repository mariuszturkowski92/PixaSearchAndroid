package com.andmar.common.navigation

interface AppNavigator : SearchNavigator, DetailsNavigator {
}

/**
 * Empty implementation of [AppNavigator] to be used in tests or previews.
 */
class EmptyAppNavigator : AppNavigator {
    override fun navigateToImageDetails(imageId: Int) {
        // no-op
    }
}
