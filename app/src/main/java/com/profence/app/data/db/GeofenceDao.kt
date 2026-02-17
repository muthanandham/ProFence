package com.profence.app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {
    @Query("SELECT * FROM geofences ORDER BY createdAt DESC")
    fun getAllGeofences(): Flow<List<GeofenceEntity>>

    @Query("SELECT * FROM geofences WHERE isActive = 1")
    suspend fun getActiveGeofencesSync(): List<GeofenceEntity>

    @Query("SELECT * FROM geofences WHERE id = :id")
    suspend fun getGeofenceById(id: Long): GeofenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(geofence: GeofenceEntity): Long

    @Update
    suspend fun updateGeofence(geofence: GeofenceEntity)

    @Delete
    suspend fun deleteGeofence(geofence: GeofenceEntity)

    @Query("DELETE FROM geofences WHERE id = :id")
    suspend fun deleteGeofenceById(id: Long)
}
