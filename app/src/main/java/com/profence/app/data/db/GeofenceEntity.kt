package com.profence.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geofences")
data class GeofenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,            // meters (100–10000)
    val transitionEnter: Boolean,
    val transitionExit: Boolean,
    val transitionDwell: Boolean,
    val dwellTimeMinutes: Int,    // 1, 5, or 10
    val isActive: Boolean,
    val lastStatus: String,       // "INSIDE", "OUTSIDE", "UNKNOWN"
    val createdAt: Long
)
