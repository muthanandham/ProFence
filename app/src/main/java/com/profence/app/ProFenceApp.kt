package com.profence.app

import android.app.Application
import com.profence.app.geofence.NotificationHelper
import org.osmdroid.config.Configuration

class ProFenceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Notification Channel
        NotificationHelper(this).createNotificationChannel()
        
        // Initialize osmdroid configuration
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", 0))
    }
}
