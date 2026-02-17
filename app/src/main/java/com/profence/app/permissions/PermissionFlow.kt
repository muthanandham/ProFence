package com.profence.app.permissions

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionFlow(
    onPermissionsGranted: () -> Unit
) {
    val foregroundPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (foregroundPermissions.allPermissionsGranted) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            BackgroundLocationPermission(onGranted = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    NotificationPermission(onGranted = onPermissionsGranted)
                } else {
                    onPermissionsGranted()
                }
            })
        } else {
            onPermissionsGranted()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Location Access Needed",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "ProFence needs your location to detect when you enter or leave geofences. Please grant 'While using the app' access.",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { foregroundPermissions.launchMultiplePermissionRequest() }) {
                Text("Grant Location Access")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BackgroundLocationPermission(onGranted: () -> Unit) {
    val backgroundPermission = rememberPermissionState(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    if (backgroundPermission.status.isGranted) {
        onGranted()
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Background Location Required",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "To detect geofences even when the app is closed, you must select 'Allow all the time' in settings.",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { backgroundPermission.launchPermissionRequest() }) {
                Text("Open Settings")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationPermission(onGranted: () -> Unit) {
    val notificationPermission = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

    if (notificationPermission.status.isGranted) {
        onGranted()
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Notifications",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "ProFence needs permission to send you alerts when you cross a geofence.",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { notificationPermission.launchPermissionRequest() }) {
                Text("Allow Notifications")
            }
        }
    }
}
