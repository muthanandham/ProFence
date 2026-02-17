package com.profence.app.data.repository

import com.profence.app.data.db.GeofenceDao
import com.profence.app.data.db.GeofenceEntity
import com.profence.app.data.db.LogDao
import com.profence.app.data.db.LogEntry
import com.profence.app.geofence.GeofenceManager
import kotlinx.coroutines.flow.Flow


class GeofenceRepository(
    private val geofenceDao: GeofenceDao,
    private val logDao: LogDao,
    private val geofenceManager: GeofenceManager
) {

    fun getAllGeofences(): Flow<List<GeofenceEntity>> = geofenceDao.getAllGeofences()

    fun getAllLogs(): Flow<List<LogEntry>> = logDao.getAllLogs()

    suspend fun addGeofence(geofence: GeofenceEntity) {
        val id = geofenceDao.insertGeofence(geofence)
        val savedGeofence = geofence.copy(id = id)
        if (savedGeofence.isActive) {
            geofenceManager.addGeofence(savedGeofence, {}, {})
        }
    }

    suspend fun removeGeofence(geofence: GeofenceEntity) {
        geofenceDao.deleteGeofence(geofence)
        geofenceManager.removeGeofence(geofence.id)
    }

    suspend fun toggleGeofence(geofence: GeofenceEntity) {
        val newStatus = !geofence.isActive
        val updatedGeofence = geofence.copy(isActive = newStatus)
        geofenceDao.updateGeofence(updatedGeofence)
        
        if (newStatus) {
            geofenceManager.addGeofence(updatedGeofence, {}, {})
        } else {
            geofenceManager.removeGeofence(geofence.id)
        }
    }
    
    suspend fun clearLogs() {
        logDao.clearLogs()
    }
}
