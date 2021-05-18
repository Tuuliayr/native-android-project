package com.example.myproject

import android.Manifest
import android.content.Context
import android.os.Build
import com.vmadalin.easypermissions.EasyPermissions

object TrackingUtility {
    fun hasLocationPermissions(context: Context) =
        // Permissions needed for android versions lower than android 10
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            // Permissions needed for android 10 and greater
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
}