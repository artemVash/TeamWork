package by.vashkevich.teamwork2.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.widget.RemoteViews
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import by.vashkevich.teamwork2.R
import by.vashkevich.teamwork2.WeatherViewModel
import by.vashkevich.teamwork2.widget.WeatherWidgetConfigureActivity.Companion.ACTION_PROGRESS_OFF

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
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        //super.onReceive(context, intent)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = loadTitlePref(context, appWidgetId)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.weather_widget)

    val intent = Intent(context, WeatherWidget::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

    val ids =
        appWidgetManager.getAppWidgetIds(ComponentName(context, WeatherWidget::class.java))

    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

    val updateIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    views.setOnClickPendingIntent(R.id.refresh_btn, updateIntent)

    val intent1 = Intent()
    val y = intent1.getDoubleExtra("pop", 111.1)


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)

}

