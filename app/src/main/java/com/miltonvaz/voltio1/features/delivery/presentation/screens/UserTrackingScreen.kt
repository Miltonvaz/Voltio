package com.miltonvaz.voltio1.features.delivery.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.miltonvaz.voltio1.features.delivery.presentation.viewmodel.UserTrackingViewModel
import com.miltonvaz.voltio1.features.products.presentation.components.AdminHeader

@Composable
fun UserTrackingScreen(
    orderId: Int,
    onBackClick: () -> Unit,
    viewModel: UserTrackingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.startTracking(orderId)
    }

    DisposableEffect(orderId) {
        onDispose {
            viewModel.stopTracking(orderId)
        }
    }

    Scaffold(
        topBar = {
            AdminHeader(
                title = "Sigue tu pedido",
                subtitle = "Pedido #$orderId",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            val defaultPos = LatLng(16.6248, -93.1025)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(uiState.driverLocation ?: defaultPos, 15f)
            }

            LaunchedEffect(uiState.driverLocation) {
                uiState.driverLocation?.let {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(it))
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                uiState.driverLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Tu repartidor",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFCED9ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = Color(0xFF455E91),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = "Repartidor en camino",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color(0xFF1A1C2E)
                        )
                        Text(
                            text = if (uiState.driverLocation != null) "Sigue el movimiento en vivo" else "Esperando señal del GPS...",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF455E91)
                )
            }
        }
    }
}
