package com.miltonvaz.voltio1.features.directions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.miltonvaz.voltio1.core.ui.theme.displayFontFamily
import com.miltonvaz.voltio1.features.directions.data.datasource.remote.model.DirectionRequest
import com.miltonvaz.voltio1.features.directions.domain.entities.Direction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectionFormSheet(
    directionToEdit: Direction? = null,
    onDismiss: () -> Unit,
    onSave: (DirectionRequest) -> Unit
) {
    var alias by remember { mutableStateOf(directionToEdit?.alias ?: "") }
    var direccion by remember { mutableStateOf(directionToEdit?.direccion ?: "") }
    var esPredeterminada by remember { mutableStateOf(directionToEdit?.es_predeterminada ?: false) }
    
    var selectedLocation by remember { 
        mutableStateOf(
            if (directionToEdit?.latitude != null && directionToEdit.longitude != null) {
                LatLng(directionToEdit.latitude, directionToEdit.longitude)
            } else {
                LatLng(16.7528, -93.1154)
            }
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 15f)
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1E293B),
        unfocusedTextColor = Color(0xFF1E293B),
        focusedLabelColor = Color(0xFF4F46E5),
        unfocusedLabelColor = Color(0xFF64748B),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color(0xFFFAFAFC),
        focusedBorderColor = Color(0xFF4F46E5),
        unfocusedBorderColor = Color(0xFFE2E8F0)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE0E7FF),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.LocationOn, null,
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (directionToEdit != null) "Editar dirección" else "Nueva dirección",
                        fontFamily = displayFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Toca el mapa para ubicar el punto exacto",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))

            // Interactive map
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 4.dp
            ) {
                Box {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapClick = { selectedLocation = it },
                        uiSettings = MapUiSettings(zoomControlsEnabled = false)
                    ) {
                        Marker(
                            state = MarkerState(position = selectedLocation),
                            title = "Punto de entrega",
                            draggable = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            OutlinedTextField(
                value = alias,
                onValueChange = { alias = it },
                label = { Text("Alias (ej. Casa, Trabajo)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Referencia o dirección escrita") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                minLines = 2
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Checkbox(
                        checked = esPredeterminada,
                        onCheckedChange = { esPredeterminada = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF4F46E5),
                            uncheckedColor = Color(0xFFCBD5E1)
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Establecer como dirección principal",
                        fontFamily = displayFontFamily,
                        fontSize = 14.sp,
                        color = Color(0xFF1E293B),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    onSave(
                        DirectionRequest(
                            id_usuario = 0,
                            alias = alias.ifBlank { null },
                            direccion = direccion,
                            latitude = selectedLocation.latitude,
                            longitude = selectedLocation.longitude,
                            es_predeterminada = esPredeterminada
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F46E5),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                enabled = direccion.isNotBlank()
            ) {
                Text(
                    text = if (directionToEdit != null) "GUARDAR CAMBIOS" else "AGREGAR DIRECCIÓN",
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}