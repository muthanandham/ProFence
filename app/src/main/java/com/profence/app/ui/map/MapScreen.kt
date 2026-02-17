package com.profence.app.ui.map

import android.view.MotionEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.profence.app.ui.ProFenceViewModel
import com.profence.app.ui.fenceconfig.FenceConfigSheet
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen(viewModel: ProFenceViewModel) {
    val context = LocalContext.current
    val geofences by viewModel.allGeofences.collectAsState(initial = emptyList())
    
    // State for creating new fence
    var showSheet by remember { mutableStateOf(false) }
    var newFenceLat by remember { mutableDoubleStateOf(0.0) }
    var newFenceLng by remember { mutableDoubleStateOf(0.0) }

    // Initialize osmdroid configuration
    DisposableEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        onDispose { }
    }

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                
                // Add MyLocation overlay
                val locationOverlay = MyLocationNewOverlay(this)
                locationOverlay.enableMyLocation()
                locationOverlay.enableFollowLocation()
                overlays.add(locationOverlay)
                
                // Map Events for Long Press
                val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean = false
                    
                    override fun longPressHelper(p: GeoPoint?): Boolean {
                        p?.let {
                            newFenceLat = it.latitude
                            newFenceLng = it.longitude
                            showSheet = true
                        }
                        return true
                    }
                })
                overlays.add(mapEventsOverlay)
            }
        },
        update = { mapView ->
            // Clear existing polygons (except MyLocation and Events)
            // A better way is to manage a specific folder overlay, but for simplicity:
            // removing only Polygons
            mapView.overlays.removeIf { it is Polygon }
            
            geofences.filter { it.isActive }.forEach { fence ->
                val circle = Polygon().apply {
                    points = Polygon.pointsAsCircle(GeoPoint(fence.latitude, fence.longitude), fence.radius.toDouble())
                    fillPaint.color = 0x4000E676 // Transparent Green
                    fillPaint.style = android.graphics.Paint.Style.FILL
                    outlinePaint.color = 0xFF00E676.toInt()
                    outlinePaint.strokeWidth = 2f
                }
                mapView.overlays.add(circle)
            }
            
            mapView.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )

    if (showSheet) {
        FenceConfigSheet(
            lat = newFenceLat,
            lng = newFenceLng,
            onDismiss = { showSheet = false },
            onSave = { geofence ->
                viewModel.addGeofence(geofence)
                showSheet = false
            }
        )
    }
}
