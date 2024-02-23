package com.andmar.pixasearch.widgets.di

import androidx.glance.appwidget.GlanceAppWidgetManager
import com.andmar.data.images.usecase.GetAllImagesUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun glanceAppWidgetManager(): GlanceAppWidgetManager
    fun getAllImagesUseCase(): GetAllImagesUseCase
}