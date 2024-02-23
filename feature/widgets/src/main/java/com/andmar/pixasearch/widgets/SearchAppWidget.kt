package com.andmar.pixasearch.widgets

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.material3.ColorProviders
import androidx.glance.text.Text
import com.andmar.data.images.entity.PSImage
import com.andmar.data.images.usecase.GetAllImagesUseCase
import com.andmar.pixasearch.widgets.di.WidgetEntryPoint
import com.andmar.ui.theme.DarkColorScheme
import com.andmar.ui.theme.LightColorScheme
import dagger.hilt.android.EntryPointAccessors

class SearchAppWidget() : GlanceAppWidget() {

    private lateinit var getAllImagesUseCase: GetAllImagesUseCase
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        if (!this::getAllImagesUseCase.isInitialized) {
            getAllImagesUseCase = getGetAllImagesUseCase(context)
        }

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            val allImages by getAllImagesUseCase().collectAsState(initial = emptyList())
            // create your AppWidget here

            GlanceTheme(
                ColorProviders(
                    dark = DarkColorScheme,
                    light = LightColorScheme
                )
            ) {
                AppWidgetContent(allImages)
            }

        }
    }

    @Composable
    private fun AppWidgetContent(allImages: List<PSImage>) {
        Box(
            modifier = GlanceModifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .appWidgetBackground()
        ) {

            LazyColumn(modifier = GlanceModifier.fillMaxWidth().padding(8.dp)) {
                item {
                    Text("Images:")
                }
                items(allImages, itemId = { it.id.toLong() }) { image ->
                    Text("${image.id}", modifier = GlanceModifier.fillMaxWidth())
                }
            }
        }
    }


}

private fun getGlanceAppWidgetManager(context: Context): GlanceAppWidgetManager {
    val hiltEntryPoint = EntryPointAccessors.fromApplication(
        context, WidgetEntryPoint::class.java
    )
    return hiltEntryPoint.glanceAppWidgetManager()
}

private fun getGetAllImagesUseCase(context: Context): GetAllImagesUseCase {
    val hiltEntryPoint = EntryPointAccessors.fromApplication(
        context, WidgetEntryPoint::class.java
    )
    return hiltEntryPoint.getAllImagesUseCase()
}