package com.profence.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.profence.app.data.db.GeofenceEntity
import com.profence.app.data.db.LogEntry
import com.profence.app.data.repository.GeofenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProFenceViewModel(private val repository: GeofenceRepository) : ViewModel() {

    val allGeofences: Flow<List<GeofenceEntity>> = repository.getAllGeofences()
    val allLogs: Flow<List<LogEntry>> = repository.getAllLogs()

    fun addGeofence(geofence: GeofenceEntity) {
        viewModelScope.launch {
            repository.addGeofence(geofence)
        }
    }

    fun removeGeofence(geofence: GeofenceEntity) {
        viewModelScope.launch {
            repository.removeGeofence(geofence)
        }
    }

    fun toggleGeofence(geofence: GeofenceEntity) {
        viewModelScope.launch {
            repository.toggleGeofence(geofence)
        }
    }
}

class ProFenceViewModelFactory(private val repository: GeofenceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProFenceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProFenceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
