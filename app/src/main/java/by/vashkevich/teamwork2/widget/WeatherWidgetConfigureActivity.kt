package by.vashkevich.teamwork2.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.vashkevich.teamwork2.R
import by.vashkevich.teamwork2.WeatherViewModel

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
class WeatherWidgetConfigureActivity : Activity() {

    private val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application).create(WeatherViewModel::class.java)
    }

    companion object {
        val ACTION_PROGRESS_OFF = "action"
    }

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var latitudeText: EditText
    private lateinit var longitudeText: EditText

    private var onClickListener = View.OnClickListener {
        val context = this@WeatherWidgetConfigureActivity


        var temp: Double
        //var temp1: Double
        //val array = ArrayList<Any>()

        viewModel.daysLiveData.observeForever {
            temp = it.daily[0].temp.dayTemp
            //temp1 = it.daily[0].temp.nightTemp


            val intent = Intent(ACTION_PROGRESS_OFF)
            intent.putExtra("pop", temp)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

            // val y = Intent().putExtra("pop", temp)

        }

        viewModel.load(
            latitudeText.text.toString().toDouble(),
            longitudeText.text.toString().toDouble()
        )


        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    @SuppressLint("WrongViewCast")
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        setContentView(R.layout.weather_widget_configure)
        latitudeText = findViewById<View>(R.id.add_latitude_text) as EditText
        longitudeText = findViewById<View>(R.id.add_longitude_text) as EditText
        findViewById<View>(R.id.add_button).setOnClickListener(onClickListener)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
    }
}

private const val PREFS_NAME = "by.vashkevich.teamwork2.widget.WeatherWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    return titleValue ?: context.getString(R.string.appwidget_text)
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}
