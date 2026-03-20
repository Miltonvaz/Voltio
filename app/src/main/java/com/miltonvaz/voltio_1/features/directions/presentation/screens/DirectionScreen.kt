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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.core.ui.theme.displayFontFamily
import com.miltonvaz.voltio_1.features.directions.domain.entities.Direction
import com.miltonvaz.voltio_1.features.directions.presentation.components.DirectionCard
import com.miltonvaz.voltio_1.features.directions.presentation.components.DirectionFormSheet
import com.miltonvaz.voltio_1.features.directions.presentation.viewmodel.DirectionViewModel
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CheckoutUiState
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.CheckoutViewModel
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader

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
                reference = direction.alias
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (checkoutState != null && selectedDirectionId != null) {
                    ExtendedFloatingActionButton(
                        onClick = onPlaceOrder,
                        containerColor = Color(0xFFA0BBF8),
                        contentColor = Color(0xFF1A1C2E),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        if (checkoutState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color(0xFF1A1C2E))
                        } else {
                            Text(text = "FINALIZAR PEDIDO", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = Color(0xFFA0BBF8),
                    contentColor = Color(0xFF1A1C2E),
                    shape = RoundedCornerShape(16.dp)
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
                            CircularProgressIndicator(color = Color(0xFF455E91))
                        }
                    }
                    uiState.directions.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Sin direcciones", fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C2E))
                                Text("Agrega tu primera dirección de envío", fontSize = 13.sp, color = Color.Gray)
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
                                val isSelected = direction.id == selectedDirectionId
                                DirectionCard(
                                    direction = direction,
                                    onEdit = { onEditClick(direction) },
                                    onDelete = { onDeleteClick(direction.id) },
                                    onClick = { onDirectionClick(direction) }
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(Color(0xFF455E91))
                                    )
                                }
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
        Direction(1, 1, "Casa", "Av. Principal 123, Suchiapa", true, ""),
        Direction(2, 1, "Trabajo", "Calle Secundaria 456, Tuxtla", false, "")
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
