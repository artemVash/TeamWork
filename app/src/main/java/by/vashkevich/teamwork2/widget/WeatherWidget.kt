package by.vashkevich.teamwork2.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import by.vashkevich.teamwork2.R
import by.vashkevich.teamwork2.data.entities.weather.Days
import by.vashkevich.teamwork2.repositories.weather.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [WeatherWidgetConfigureActivity]
 */
class WeatherWidget : AppWidgetProvider() {

    companion object {
        const val TAG = "tag"
    }

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
}

private fun load(
    lat: Double,
    lon: Double,
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val repository = WeatherRepository.getRepository()
    val ioScope = CoroutineScope(Dispatchers.IO)
    ioScope.launch {
        val resultDays: Days? = try {
            repository.loadWeather(lat, lon)
        } catch (e: Exception) {
            null
        }
        Log.d("Weather", "Result $resultDays")
        withContext(Dispatchers.Main) {
            // Construct the RemoteViews object
            if (resultDays != null) {
                val views = RemoteViews(context.packageName, R.layout.weather_widget)

                val arrayDataView = listOf(
                    R.id.data1,
                    R.id.data2,
                    R.id.data3,
                    R.id.data4
                )
                val arrayIconView = listOf(
                    R.id.icon1,
                    R.id.icon2,
                    R.id.icon3,
                    R.id.icon4
                )
                val arrayTemperatureView = listOf(
                    R.id.temperature1,
                    R.id.temperature2,
                    R.id.temperature3,
                    R.id.temperature4
                )

                for (numberDataView in arrayDataView) {
                    val dayDate = setDateFormat(
                        resultDays.daily[arrayDataView.indexOf(numberDataView)]
                            .dt
                            .toLong()
                    )
                    views.setTextViewText(numberDataView, dayDate)
                }

                for (numberTempView in arrayTemperatureView) {
                    val dayTemp = resultDays.daily[arrayTemperatureView.indexOf(numberTempView)]
                        .temp
                        .dayTemp
                        .toInt()
                        .toString()
                    val nightTemp = resultDays.daily[arrayTemperatureView.indexOf(numberTempView)]
                        .temp
                        .nightTemp
                        .toInt()
                        .toString()
                    views.setTextViewText(
                        numberTempView,
                        "$dayTemp°/$nightTemp°"
                    )
                }

                for (numberIconView in arrayIconView) {
                    val icon = resultDays.daily[arrayIconView.indexOf(numberIconView)]
                        .weather[0]
                        .icon
                    views.setImageViewResource(numberIconView, setImage(icon))
                }
                // Instruct the widget manager to update the widget
                val intent = Intent(context, WeatherWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                val ids = appWidgetManager.getAppWidgetIds(
                    ComponentName(
                        context,
                        WeatherWidget::class.java
                    )
                )

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

                val updateIntent = PendingIntent.getBroadcast(
                    context,
                    appWidgetId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                views.setOnClickPendingIntent(R.id.refresh_btn, updateIntent)

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}

private fun setDateFormat(time: Long): String {
    val current = time * 1000
    val date = Date(current)
    val simpleDateFormat = SimpleDateFormat("d.MM", Locale.getDefault())
    return simpleDateFormat.format(date)
}

private fun setImage(icon: String): Int {
    return when (icon) {
        "01d" -> R.drawable.d01
        "02d" -> R.drawable.d02
        "03d" -> R.drawable.d03
        "04d" -> R.drawable.d04
        "09d" -> R.drawable.d09
        "10d" -> R.drawable.d10
        "11d" -> R.drawable.d11
        "13d" -> R.drawable.d13
        "50d" -> R.drawable.d50
        else -> R.drawable.error_image
    }
}
