package com.profence.app.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.profence.app.data.db.AppDatabase
import com.profence.app.data.db.LogEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return
        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "Geofencing error: ${geofencingEvent.errorCode}")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        val triggeringGeofences = geofencingEvent.triggeringGeofences ?: return

        val transitionTypeStr = when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "ENTER"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "EXIT"
            Geofence.GEOFENCE_TRANSITION_DWELL -> "DWELL"
            else -> return
        }
        
        val db = AppDatabase.getDatabase(context)
        val logDao = db.logDao()
        val geofenceDao = db.geofenceDao()
        val notificationHelper = NotificationHelper(context)
        
        CoroutineScope(Dispatchers.IO).launch {
            triggeringGeofences.forEach { fence ->
                val geofenceId = fence.requestId.toLongOrNull() ?: return@forEach
                val entity = geofenceDao.getGeofenceById(geofenceId) ?: return@forEach

                // Log the event
                val logEntry = LogEntry(
                    geofenceId = geofenceId,
                    geofenceName = entity.name,
                    transitionType = transitionTypeStr,
                    timestamp = System.currentTimeMillis()
                )
                logDao.insertLog(logEntry)

                // Update status if Enter or Exit
                if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    geofenceDao.updateGeofence(entity.copy(lastStatus = "INSIDE"))
                } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    geofenceDao.updateGeofence(entity.copy(lastStatus = "OUTSIDE"))
                }
                
                // Show notification
                val message = "You have ${transitionTypeStr.lowercase()}ed ${entity.name}"
                notificationHelper.sendGeofenceNotification("Geofence Alert", message)
            }
        }
    }
}
