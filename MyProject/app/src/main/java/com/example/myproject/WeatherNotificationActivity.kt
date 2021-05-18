package com.example.myproject

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myproject.Constants.PERMISSION_ID
import com.example.myproject.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.myproject.MainActivity.Companion.locationLat
import com.example.myproject.MainActivity.Companion.locationLong
import com.example.myproject.MainActivity.Companion.locationName
import com.example.myproject.MainActivity.Companion.temperature
import com.example.myproject.MainActivity.Companion.weather
import com.example.myproject.MainActivity.Companion.weatherDesc
import com.example.myproject.MainActivity.Companion.weatherId
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.helpers.base.PermissionsHelper
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class WeatherNotificationActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    lateinit var locNameTextView: TextView
    lateinit var weatherTextView: TextView
    lateinit var infoTextView: TextView
    lateinit var tempTextView: TextView
    lateinit var weatherImageView: ImageView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_notification)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //requestPermissions()
        getLastLocation()

        locNameTextView = findViewById(R.id.textView_locName)
        locNameTextView.setText(locationName)
        weatherTextView = findViewById(R.id.textView_weather)
        weatherTextView.setText(weather)
        infoTextView = findViewById(R.id.textView_weatherInfo)
        infoTextView.setText(weatherDesc)
        tempTextView = findViewById(R.id.textView_temp)
        tempTextView.setText(getString(R.string.temp_celsius, temperature))

        weatherImageView = findViewById(R.id.imageView_weatherIcon)
        val icon: String = getWeatherIcon(weatherId)
        weatherImageView.setImageResource(resources.getIdentifier(icon, "drawable", packageName))

    }

    fun backButtonClicked(button: View) {
        openMainActivity()
    }

    private fun openMainActivity() {
        val resultIntent = Intent(this, MainActivity::class.java)
        startActivity(resultIntent)
    }

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

    /*private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }*/

    private fun getLastLocation() {
        // check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // if permission granted
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                var location: Location? = task.result
                if (location == null) {

                } else {
                    println(location.latitude)
                    println(location.longitude)
                    locationLat = location.latitude
                    locationLong = location.longitude
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

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
        /*else {
            requestPermissions()
        }*/
    }

    // Not used but required by the interface
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}