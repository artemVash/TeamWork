package by.vashkevich.teamwork2.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import by.vashkevich.teamwork2.R
import by.vashkevich.teamwork2.repositories.weather.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [WeatherWidgetConfigureActivity]
 */
class WeatherWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        appWidgetIds.forEach { appWidgetId ->
            deleteLatitude(context, appWidgetId)
            deleteLongitude(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val lat: Double = loadLatitude(context, appWidgetId).toDouble()
    val lon: Double = loadLongitude(context, appWidgetId).toDouble()
    load(lat, lon, context, appWidgetManager, appWidgetId)
    //здесь не нужно больше ничего писать, все вставляется во вьюхи в методе ниже
}

private fun load(
    lat: Double,
    lon: Double,
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    //классика .репозиторий .скоуп
    val repository = WeatherRepository.getRepository()
    val ioScope = CoroutineScope(Dispatchers.IO)
    ioScope.launch {
        val resultDays = try {
            repository.loadWeather(lat, lon)
        } catch (e: Exception) {
            Log.e("Weather", "Error in loadWeather", e)
        }
        Log.d("Weather", "Result $resultDays")
        withContext(Dispatchers.Main) {
            // Construct the RemoteViews object

            val views = RemoteViews(context.packageName, R.layout.weather_widget)
//             views.setTextViewText(R.id.appwidget_TEXT, resultDays.toString())
            //TODO resultDays вставлять уже во вьюхи лайаута

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}