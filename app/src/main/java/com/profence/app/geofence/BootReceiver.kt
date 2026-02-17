package com.profence.app.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.profence.app.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Boot completed, re-registering geofences")
            val db = AppDatabase.getDatabase(context)
            val geofenceDao = db.geofenceDao()
            val geofenceManager = GeofenceManager(context)

            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val activeGeofences = geofenceDao.getActiveGeofencesSync()
                activeGeofences.forEach { geofence ->
                    geofenceManager.addGeofence(geofence, {}, {})
                }
            }
        }
    }
}
