package com.miltonvaz.voltio1.features.directions.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio1.features.directions.domain.entities.Direction
import com.miltonvaz.voltio1.features.directions.presentation.components.DirectionCard
import com.miltonvaz.voltio1.features.directions.presentation.components.DirectionFormSheet
import com.miltonvaz.voltio1.features.directions.presentation.viewmodel.DirectionViewModel
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.CheckoutUiState
import com.miltonvaz.voltio1.features.orders.presentation.viewmodel.CheckoutViewModel
import com.miltonvaz.voltio1.features.products.presentation.components.AdminHeader

@Composable
fun DirectionScreen(
    onBackClick: () -> Unit,
    onFinishOrder: (() -> Unit)? = null,
    viewModel: DirectionViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val checkoutState by checkoutViewModel.uiState.collectAsStateWithLifecycle()
    
    var showSheet by remember { mutableStateOf(false) }
    var directionToEdit by remember { mutableStateOf<Direction?>(null) }
    var selectedDirectionId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            showSheet = false
            directionToEdit = null
            viewModel.clearCreateSuccess()
        }
    }

    LaunchedEffect(checkoutState.orderPlacedSuccessfully) {
        if (checkoutState.orderPlacedSuccessfully == true) {
            onFinishOrder?.invoke()
        }
    }

    DirectionScreenContent(
        uiState = uiState,
        checkoutState = checkoutState,
        selectedDirectionId = selectedDirectionId,
        onBackClick = onBackClick,
        onDirectionClick = { direction ->
            selectedDirectionId = direction.id
            checkoutViewModel.updateAddressInfo(
                street = direction.direccion,
                reference = direction.alias,
                latitude = direction.latitude,
                longitude = direction.longitude
            )
        },
        onPlaceOrder = { checkoutViewModel.placeOrder() },
        onAddClick = {
            directionToEdit = null
            showSheet = true
        },
        onEditClick = {
            directionToEdit = it
            showSheet = true
        },
        onDeleteClick = { viewModel.deleteDirection(it) },
        onClearError = { viewModel.clearError() }
    )

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

@Composable
fun DirectionScreenContent(
    uiState: DirectionUiState,
    checkoutState: CheckoutUiState? = null,
    selectedDirectionId: Int? = null,
    onBackClick: () -> Unit,
    onDirectionClick: (Direction) -> Unit = {},
    onPlaceOrder: () -> Unit = {},
    onAddClick: () -> Unit,
    onEditClick: (Direction) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onClearError: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (checkoutState != null && selectedDirectionId != null) {
                    ExtendedFloatingActionButton(
                        onClick = onPlaceOrder,
                        containerColor = Color(0xFF4F46E5),
                        contentColor = Color.White,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.padding(bottom = 4.dp),
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
                    ) {
                        if (checkoutState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "FINALIZAR PEDIDO", fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp)
                        }
                    }
                }
                
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = Color(0xFF4F46E5),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
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
                AdminHeader(
                    title = "Mis direcciones",
                    subtitle = if (checkoutState != null) "Selecciona una para tu pedido" else "${uiState.directions.size} dirección(es) guardada(s)",
                    onBackClick = onBackClick,
                    showProfile = true,
                    showCart = false
                )

                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF4F46E5))
                        }
                    }
                    uiState.directions.isEmpty() -> {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Surface(
                                    modifier = Modifier.size(120.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFE0E7FF).copy(alpha = 0.4f)
                                ) {}
                                Surface(
                                    modifier = Modifier.size(90.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFE0E7FF)
                                ) {}
                                Icon(
                                    Icons.Default.MyLocation, null,
                                    modifier = Modifier.size(40.dp),
                                    tint = Color(0xFF4F46E5)
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                "Sin direcciones",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1E293B),
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Agrega tu primera dirección\nde envío tocando el botón +",
                                fontSize = 14.sp,
                                color = Color(0xFF94A3B8),
                                textAlign = TextAlign.Center,
                                lineHeight = 21.sp
                            )
                        }
                    }
                    else -> {
                        if (checkoutState != null && selectedDirectionId == null) {
                            Surface(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFFF7ED)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.LocationOn, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Selecciona una dirección para continuar",
                                        fontSize = 13.sp,
                                        color = Color(0xFF92400E),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.directions) { direction ->
                                val isSelected = direction.id == selectedDirectionId
                                DirectionCard(
                                    direction = direction,
                                    isSelected = isSelected,
                                    onEdit = { onEditClick(direction) },
                                    onDelete = { onDeleteClick(direction.id) },
                                    onClick = { onDirectionClick(direction) }
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.error != null || checkoutState?.error != null) {
                Snackbar(
                    modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter),
                    containerColor = Color(0xFF1E1B4B),
                    action = { TextButton(onClick = onClearError) { Text("OK", color = Color.White) } }
                ) {
                    Text(uiState.error ?: checkoutState?.error ?: "Error")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DirectionScreenPreview() {
    val mockDirections = listOf(
        Direction(
            id = 1,
            id_usuario = 1,
            alias = "Casa",
            direccion = "Av. Principal 123, Suchiapa",
            latitude = 16.6248,
            longitude = -93.1025,
            es_predeterminada = true,
            created_at = ""
        ),
        Direction(
            id = 2,
            id_usuario = 1,
            alias = "Trabajo",
            direccion = "Calle Secundaria 456, Tuxtla",
            latitude = 16.7528,
            longitude = -93.1154,
            es_predeterminada = false,
            created_at = ""
        )
    )
    
    DirectionScreenContent(
        uiState = DirectionUiState(directions = mockDirections),
        checkoutState = CheckoutUiState(),
        selectedDirectionId = 1,
        onBackClick = {},
        onAddClick = {},
        onEditClick = {},
        onDeleteClick = {},
        onClearError = {}
    )
}
