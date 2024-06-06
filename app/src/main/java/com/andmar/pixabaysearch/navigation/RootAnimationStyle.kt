package com.andmar.pixabaysearch.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavBackStackEntry
import com.andmar.transaction.navigation.defaultDiaryEnterTransition
import com.andmar.transaction.navigation.defaultDiaryExitTransition
import com.andmar.transaction.navigation.defaultDiaryPopEnterTransition
import com.andmar.transaction.navigation.defaultDiaryPopExitTransition
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object RootAnimationStyle : NavHostAnimatedDestinationStyle() {
    override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        defaultDiaryEnterTransition(initialState, targetState)
    }

    override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        defaultDiaryExitTransition(
            initialState, targetState
        )
    }

    override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        { defaultDiaryPopEnterTransition() }
    override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        { defaultDiaryPopExitTransition() }
}