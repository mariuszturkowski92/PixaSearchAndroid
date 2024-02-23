package com.andmar.pixasearch.widgets.di

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.andmar.data.images.ImagesRepository
import com.andmar.data.images.usecase.GetAllImagesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WidgetModule {

    @Provides
    @Singleton
    fun providesGlanceWidgetManager(@ApplicationContext context: Context): GlanceAppWidgetManager =
        GlanceAppWidgetManager(context)

}
