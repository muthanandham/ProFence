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
    // 1. Foreground Location
    val foregroundPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (!foregroundPermissions.allPermissionsGranted) {
        PermissionRequestScreen(
            title = "Location Access Needed",
            message = "ProFence needs your location to detect when you enter or leave geofences. Please grant 'While using the app' access.",
            buttonText = "Grant Location Access",
            onClick = { foregroundPermissions.launchMultiplePermissionRequest() }
        )
        return
    }

    // 2. Background Location (Only for Android 10/Q+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val backgroundPermission = rememberPermissionState(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        if (!backgroundPermission.status.isGranted) {
            PermissionRequestScreen(
                title = "Background Location Required",
                message = "To detect geofences even when the app is closed, you must select 'Allow all the time' in settings.",
                buttonText = "Open Settings",
                onClick = { backgroundPermission.launchPermissionRequest() }
            )
            return
        }
    }

    // 3. Notifications (Only for Android 13/Tiramisu+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermission = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        if (!notificationPermission.status.isGranted) {
            PermissionRequestScreen(
                title = "Notifications",
                message = "ProFence needs permission to send you alerts when you cross a geofence.",
                buttonText = "Allow Notifications",
                onClick = { notificationPermission.launchPermissionRequest() }
            )
            return
        }
    }

    // All granted
    LaunchedEffect(Unit) {
        onPermissionsGranted()
    }
}

@Composable
fun PermissionRequestScreen(
    title: String,
    message: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onClick) {
            Text(buttonText)
        }
    }
}
