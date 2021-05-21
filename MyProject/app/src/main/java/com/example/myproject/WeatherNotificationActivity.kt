package com.example.myproject

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myproject.Constants.PERMISSION_ID
import com.example.myproject.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.muddzdev.styleabletoast.StyleableToast
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class WeatherNotificationActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    var locationName : String? = null
    var weather : String? = null
    var weatherDesc : String? = null
    var temperature : Int = 0
    var weatherId : Int = 0
    var locationLat : Double = 0.0
    var locationLong : Double = 0.0

    lateinit var locNameTextView: TextView
    lateinit var weatherTextView: TextView
    lateinit var infoTextView: TextView
    lateinit var tempTextView: TextView
    lateinit var weatherImageView: ImageView
    private lateinit var notificationManager : NotificationManagerCompat
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val receiver : BroadcastReceiver = MyBroadcastReceiver()
    private var gpsStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_notification)

        notificationManager = NotificationManagerCompat.from(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        registerReceiver(receiver, IntentFilter("location"))

        locNameTextView = findViewById(R.id.textView_locName)
        weatherTextView = findViewById(R.id.textView_weather)
        infoTextView = findViewById(R.id.textView_weatherInfo)
        tempTextView = findViewById(R.id.textView_temp)
        weatherImageView = findViewById(R.id.imageView_weatherIcon)
        tempTextView.text = ""

        requestPermissions()
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }

    fun backButtonClicked(button: View) {
        openMainActivity()
    }

    private fun openMainActivity() {
        val resultIntent = Intent(this, MainActivity::class.java)
        startActivity(resultIntent)
    }

    /*
    * Get current weather from OpenWeatherMap using user's coordinates
     */
    private fun getApiData() {
        thread() {
            // put json data in string
            val weatherJson : String? = getUrl("https://api.openweathermap.org/data/2.5/weather?lat=$locationLat&lon=$locationLong&appid=d68bb4c3e7ff8e28188bb13262e0fe76&units=metric")
            val mapper = ObjectMapper()
            // deserialize weatherJson
            val weatherObject: WeatherJsonObject = mapper.readValue(
                weatherJson,
                WeatherJsonObject::class.java
            )

            locationName = weatherObject.name
            temperature = weatherObject.main.temp.toInt()

            val currentWeather: MutableList<WeatherInfo>? = weatherObject.weather
            currentWeather?.forEach {
                weather = it.main
                weatherDesc = it.description
                weatherId = it.id
            }

            // Change view according to resulted data
            runOnUiThread {
                locNameTextView.text = locationName
                weatherTextView.text = weather
                infoTextView.text = weatherDesc
                tempTextView.text = getString(R.string.temp_celsius, temperature)

                val icon: String = getWeatherIcon(weatherId)
                weatherImageView.setImageResource(
                    resources.getIdentifier(
                        icon,
                        "drawable",
                        packageName
                    )
                )
            }
        }
    }

    /*
    * Open connection, read result of the stream and return as string
     */
    private fun getUrl(url: String) : String? {
        var result : String? = ""
        val url: URL = URL(url)
        val conn = url.openConnection() as HttpURLConnection
        try {
            result = conn.inputStream.bufferedReader().use {it.readText()}
        } catch (e: Exception) {
            println(e)
        } finally {
            conn.disconnect()
        }
        return result
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherInfo(
        var id: Int = 0,
        var main: String? = null,
        var description: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherTemp(var temp: Double = 0.0)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherJsonObject(
        var name: String? = null,
        var weather: MutableList<WeatherInfo>? = null,
        var main: WeatherTemp = WeatherTemp(
            0.0
        )
    )

    // Find icon by id and return the name
    private fun getWeatherIcon(id: Int): String {
        val idString = id.toString()
        var icon = ""
        val idFirstChar = idString.first()
        val idSecondChar = idString[1]
        // put image names in a list
        val weatherIcons = listOf(
            "ic_01d", "ic_02d", "ic_03d", "ic_04d", "ic_09d", "ic_10d",
            "ic_11d", "ic_13d", "ic_50d"
        )
        when (idFirstChar) {
            // Thunderstorm
            '2' -> icon = weatherIcons[6]
            // Drizzle
            '3' -> icon = weatherIcons[4]
            // Rain
            '5' -> icon = when (idSecondChar) {
                '0' -> weatherIcons[5]
                '2', '3' -> weatherIcons[4]
                else -> weatherIcons[7]
            }
            // Snow
            '6' -> icon = weatherIcons[7]
            // Atmosphere
            '7' -> weatherIcons[8]
            // Clear and clouds
            '8' -> icon = when (id) {
                800 -> weatherIcons[0]
                801 -> weatherIcons[1]
                802 -> weatherIcons[2]
                else -> weatherIcons[3]
            }
            else -> {
                print("No icon found with id $id")
            }
        }
        return icon
    }

    // Check if location services is enabled
    private fun locationEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /*
    * Get location coordinates
     */
    private fun getLastLocation() {
        locationEnabled()
        // if location is enabled
        if (gpsStatus) {
            // check permission
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // if permission granted
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    thread() {
                        val location: Location? = task.result
                        if (location != null) {
                            println(location.latitude)
                            println(location.longitude)
                            locationLat = location.latitude
                            locationLong = location.longitude

                            getApiData()
                        }
                    }
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSION_ID
                )
            }
            // if location services is disabled, toast
        } else {
            val toastText = getString(R.string.no_location)
            StyleableToast.makeText(this, toastText, R.style.toast_style).show()
        }

    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(this)) {
            return
        }
        // Check android version again if user denied permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this function of the app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this function of the app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    // Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
    // This will display a dialog directing them to enable the permission in app settings.
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    // Not used but required by the interface
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // Does this every time broadcast is sent
    inner class MyBroadcastReceiver : BroadcastReceiver() {
        @ExperimentalStdlibApi
        override fun onReceive(context: Context?, intent: Intent?) {
            getLastLocation()
            locationEnabled()

            if (gpsStatus) {
                if (weather != null) {
                    val weatherTemp = weather

                    if (weatherTemp?.lowercase() == "clear") {
                        createNotification()
                    }
                }
            }
        }
    }

    /*
    * Notification for clear weather
     */
    private fun createNotification() {
        // Open activity when notification is tapped
        val resultIntent = Intent(this, WeatherNotificationActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, Constants.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_strength)
            .setContentTitle("Great weather to go out!")
            .setContentText("$weather $temperature °C")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}