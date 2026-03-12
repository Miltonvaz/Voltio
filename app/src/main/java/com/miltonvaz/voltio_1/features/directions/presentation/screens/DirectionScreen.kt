package com.miltonvaz.voltio_1.features.directions.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.core.ui.theme.displayFontFamily
import com.miltonvaz.voltio_1.features.directions.domain.entities.Direction
import com.miltonvaz.voltio_1.features.directions.presentation.components.DirectionCard
import com.miltonvaz.voltio_1.features.directions.presentation.components.DirectionFormSheet
import com.miltonvaz.voltio_1.features.directions.presentation.viewmodel.DirectionViewModel

@Composable
fun DirectionScreen(
    onBackClick: () -> Unit,
    onContinueToCheckout: (() -> Unit)? = null,
    viewModel: DirectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSheet by remember { mutableStateOf(false) }
    var directionToEdit by remember { mutableStateOf<Direction?>(null) }

    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            showSheet = false
            directionToEdit = null
            viewModel.clearCreateSuccess()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (onContinueToCheckout != null && uiState.directions.isNotEmpty()) {
                    ExtendedFloatingActionButton(
                        onClick = onContinueToCheckout,
                        containerColor = Color(0xFF4F46E5),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Continuar",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                FloatingActionButton(
                    onClick = {
                        directionToEdit = null
                        showSheet = true
                    },
                    containerColor = Color(0xFF4F46E5),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8FAFC))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF4F46E5))
                        .padding(horizontal = 24.dp)
                        .padding(top = 48.dp, bottom = 32.dp)
                ) {
                    Column {
                        TextButton(onClick = onBackClick) {
                            Text("← Volver", color = Color.White, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mis direcciones",
                            fontFamily = displayFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${uiState.directions.size} dirección(es) guardada(s)",
                            fontFamily = displayFontFamily,
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF4F46E5))
                        }
                    }
                    uiState.directions.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Sin direcciones",
                                    fontFamily = displayFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1A1C2E)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Agrega tu primera dirección de envío",
                                    fontFamily = displayFontFamily,
                                    fontSize = 13.sp,
                                    color = Color(0xFF616161)
                                )
                            }
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.directions) { direction ->
                                DirectionCard(
                                    direction = direction,
                                    onEdit = {
                                        directionToEdit = it
                                        showSheet = true
                                    },
                                    onDelete = { viewModel.deleteDirection(it) }
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.error != null) {
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    containerColor = Color(0xFF1E1B4B),
                    contentColor = Color.White,
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK", color = Color.White)
                        }
                    }
                ) {
                    Text(uiState.error ?: "Error desconocido")
                }
            }
        }
    }

    if (showSheet) {
        DirectionFormSheet(
            directionToEdit = directionToEdit,
            onDismiss = {
                showSheet = false
                directionToEdit = null
            },
            onSave = { request ->
                if (directionToEdit != null) {
                    viewModel.updateDirection(directionToEdit!!.id, request)
                } else {
                    viewModel.createDirection(request)
                }
            }
        )
    }
}