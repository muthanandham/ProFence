package com.profence.app.ui.fenceconfig

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.profence.app.data.db.GeofenceEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FenceConfigSheet(
    lat: Double,
    lng: Double,
    onDismiss: () -> Unit,
    onSave: (GeofenceEntity) -> Unit
) {
    var name by remember { mutableStateOf("New Fence") }
    var radius by remember { mutableFloatStateOf(100f) }
    var enter by remember { mutableStateOf(true) }
    var exit by remember { mutableStateOf(true) }
    var dwell by remember { mutableStateOf(false) }
    var dwellTimeIndex by remember { mutableIntStateOf(0) } // 0 -> 1 min, 1 -> 5 min, 2 -> 10 min
    
    val dwellOptions = listOf(1, 5, 10)

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp) // Extra padding for navigation bar
        ) {
            Text("Create Geofence", style = MaterialTheme.typography.titleLarge)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Fence Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Radius: ${radius.toInt()}m")
            Slider(
                value = radius, 
                onValueChange = { radius = it },
                valueRange = 100f..10000f,
                steps = 99 // Roughly 100m steps
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Triggers", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = enter, onCheckedChange = { enter = it })
                Text("Enter")
                Spacer(modifier = Modifier.width(16.dp))
                Checkbox(checked = exit, onCheckedChange = { exit = it })
                Text("Exit")
                Spacer(modifier = Modifier.width(16.dp))
                Checkbox(checked = dwell, onCheckedChange = { dwell = it })
                Text("Dwell")
            }
            
            if (dwell) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Dwell Time (minutes)")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    dwellOptions.forEachIndexed { index, min ->
                        FilterChip(
                            selected = dwellTimeIndex == index,
                            onClick = { dwellTimeIndex = index },
                            label = { Text("$min min") }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    val geofence = GeofenceEntity(
                        name = name,
                        latitude = lat,
                        longitude = lng,
                        radius = radius,
                        transitionEnter = enter,
                        transitionExit = exit,
                        transitionDwell = dwell,
                        dwellTimeMinutes = dwellOptions[dwellTimeIndex],
                        isActive = true,
                        lastStatus = "UNKNOWN",
                        createdAt = System.currentTimeMillis()
                    )
                    onSave(geofence)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Create Fence")
            }
        }
    }
}
