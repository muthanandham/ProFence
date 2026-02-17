package com.profence.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.profence.app.data.db.AppDatabase
import com.profence.app.data.repository.GeofenceRepository
import com.profence.app.geofence.GeofenceManager
import com.profence.app.permissions.PermissionFlow
import com.profence.app.ui.ProFenceViewModel
import com.profence.app.ui.ProFenceViewModelFactory
import com.profence.app.ui.navigation.AppNavigation
import com.profence.app.ui.theme.ProFenceTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: GeofenceRepository
    
    private val viewModel: ProFenceViewModel by viewModels {
        ProFenceViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual DI
        val db = AppDatabase.getDatabase(applicationContext)
        val geofenceManager = GeofenceManager(applicationContext)
        repository = GeofenceRepository(db.geofenceDao(), db.logDao(), geofenceManager)
        
        setContent {
            ProFenceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var permissionsGranted by remember { mutableStateOf(false) }
                    
                    if (permissionsGranted) {
                        AppNavigation(viewModel = viewModel)
                    } else {
                        PermissionFlow(onPermissionsGranted = {
                            permissionsGranted = true
                        })
                    }
                }
            }
        }
    }
}
