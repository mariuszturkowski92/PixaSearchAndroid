package com.andmar.pixasearch.widgets

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.andmar.data.images.usecase.GetAllImagesUseCase
import com.andmar.pixasearch.widgets.di.WidgetEntryPoint
import dagger.hilt.android.EntryPointAccessors

class SearchWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SearchAppWidget()
}
