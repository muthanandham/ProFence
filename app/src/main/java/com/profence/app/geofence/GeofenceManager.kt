package com.profence.app.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.profence.app.data.db.GeofenceEntity

class GeofenceManager(private val context: Context) {

    private val geofencingClient = LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        // We must use FLAG_MUTABLE for Android 12+ (API 31) when dealing with Location Services 
        // if we want the intent to be mutable, but here immutable is generally safer unless we need to modify it.
        // However, Geofencing implementation often requires MUTABLE for some older patterns, 
        // but typically for Receivers, IMMUTABLE is preferred unless the system needs to fill in data.
        // The documentation for GeofencingClient says: "The PendingIntent must be mutable if the application targets Android 12 or higher" 
        // Wait, checking docs... Actually, `PendingIntent.FLAG_MUTABLE` is required for some location broadcasts.
        // Let's use FLAG_MUTABLE since the system fills in the GeofencingEvent.
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        PendingIntent.getBroadcast(context, 0, intent, flags)
    }

    @SuppressLint("MissingPermission")
    fun addGeofence(geofence: GeofenceEntity, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        if (!geofence.isActive) return

        val transitionTypes = buildTransitionTypes(geofence)
        
        val gmsGeofence = Geofence.Builder()
            .setRequestId(geofence.id.toString())
            .setCircularRegion(geofence.latitude, geofence.longitude, geofence.radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(transitionTypes)
            .setLoiteringDelay(geofence.dwellTimeMinutes * 60 * 1000) // Dwell time in millis
            .build()
            
        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER) // Trigger if already inside
            .addGeofence(gmsGeofence)
            .build()
            
        geofencingClient.addGeofences(request, geofencePendingIntent)
            .addOnSuccessListener { 
                Log.d("GeofenceManager", "Geofence added: ${geofence.id}")
                onSuccess() 
            }
            .addOnFailureListener { e -> 
                Log.e("GeofenceManager", "Failed to add geofence", e)
                onFailure(e) 
            }
    }
    
    fun removeGeofence(geofenceId: Long) {
        geofencingClient.removeGeofences(listOf(geofenceId.toString()))
            .addOnSuccessListener { Log.d("GeofenceManager", "Geofence removed: $geofenceId") }
            .addOnFailureListener { Log.e("GeofenceManager", "Failed to remove geofence", it) }
    }
    
    private fun buildTransitionTypes(geofence: GeofenceEntity): Int {
        var types = 0
        if (geofence.transitionEnter) types = types or Geofence.GEOFENCE_TRANSITION_ENTER
        if (geofence.transitionExit) types = types or Geofence.GEOFENCE_TRANSITION_EXIT
        if (geofence.transitionDwell) types = types or Geofence.GEOFENCE_TRANSITION_DWELL
        return types
    }
}
