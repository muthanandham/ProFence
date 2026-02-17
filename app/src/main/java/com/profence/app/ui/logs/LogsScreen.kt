package com.profence.app.ui.logs

import android.text.format.DateFormat
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.profence.app.data.db.LogEntry
import com.profence.app.ui.ProFenceViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import java.util.Date

@Composable
fun LogsScreen(viewModel: ProFenceViewModel) {
    val logs by viewModel.allLogs.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Activity Logs", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(logs) { log ->
                LogCard(log)
            }
        }
    }
}

@Composable
fun LogCard(log: LogEntry) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(log.geofenceName, style = MaterialTheme.typography.bodyLarge)
                Text(
                    DateFormat.format("MMM dd, HH:mm", Date(log.timestamp)).toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${log.transitionType}",
                color = when(log.transitionType) {
                    "ENTER" -> MaterialTheme.colorScheme.primary
                    "EXIT" -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.secondary
                },
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
