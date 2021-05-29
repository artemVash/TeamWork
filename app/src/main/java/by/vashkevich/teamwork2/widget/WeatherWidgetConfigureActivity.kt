package by.vashkevich.teamwork2.widget

import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import by.vashkevich.teamwork2.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
class WeatherWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    //долгота.широта
    private lateinit var latText: EditText
    private lateinit var lonText: EditText

    private lateinit var fusedLocationProvider: FusedLocationProviderClient

    private var onClickListener = View.OnClickListener {
        val context = this@WeatherWidgetConfigureActivity

        // When the button is clicked, store the string locally
        val lat = latText.text.toString()
        val lon = lonText.text.toString()
        saveLatitude(context, appWidgetId, lat)
        saveLongitude(context, appWidgetId, lon)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        setContentView(R.layout.weather_widget_configure)
        latText = findViewById<View>(R.id.add_latitude_text) as EditText
        lonText = findViewById<View>(R.id.add_longitude_text) as EditText
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
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Toast.makeText(this, "Разрешение уже есть", Toast.LENGTH_SHORT).show()
                fusedLocationProvider.lastLocation.addOnCompleteListener {
                    latText.setText(it.result.latitude.toString())
                    lonText.setText(it.result.longitude.toString())
                }
            }
            else -> {
                Toast.makeText(this, "Agree to use your location in settings", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 1
    }
}

private const val PREFS_NAME = "by.vashkevich.teamwork2.widget.WeatherWidget2"
private const val PREF_PREFIX_KEY = "appwidget_"
private const val PREF_LAT_SUFFIX = "_lat"
private const val PREF_LON_SUFFIX = "_lon"

internal fun saveLatitude(context: Context, appWidgetId: Int, lat: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId + PREF_LAT_SUFFIX, lat)
    prefs.apply()
}

internal fun deleteLatitude(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId + PREF_LAT_SUFFIX)
    prefs.apply()
}

internal fun loadLatitude(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getString(PREF_PREFIX_KEY + appWidgetId + PREF_LAT_SUFFIX, null) ?: "0"
}

internal fun saveLongitude(context: Context, appWidgetId: Int, lon: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId + PREF_LON_SUFFIX, lon)
    prefs.apply()
}

internal fun deleteLongitude(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId + PREF_LON_SUFFIX)
    prefs.apply()
}

internal fun loadLongitude(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getString(PREF_PREFIX_KEY + appWidgetId + PREF_LON_SUFFIX, null) ?: "0"
}