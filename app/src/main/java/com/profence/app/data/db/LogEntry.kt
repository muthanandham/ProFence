package com.profence.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val geofenceId: Long,
    val geofenceName: String,
    val transitionType: String,   // "ENTER", "EXIT", "DWELL"
    val timestamp: Long
)
