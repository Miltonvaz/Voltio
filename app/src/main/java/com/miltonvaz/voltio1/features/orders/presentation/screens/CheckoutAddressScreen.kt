package com.miltonvaz.voltio1.features.orders.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MarkunreadMailbox
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.AddressInfo
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.CheckoutUiState
import com.miltonvaz.voltio1.features.orders.presentation.viewmodel.CheckoutViewModel
import com.miltonvaz.voltio1.features.products.presentation.components.AdminHeader

@Composable
fun CheckoutAddressScreen(
    onBackClick: () -> Unit = {},
    onFinishOrder: () -> Unit = {},
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.orderPlacedSuccessfully) {
        if (uiState.orderPlacedSuccessfully == true) {
            onFinishOrder()
        }
    }

    CheckoutAddressScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onStreetChange = { viewModel.updateAddressInfo(street = it) },
        onCityChange = { viewModel.updateAddressInfo(city = it) },
        onStateChange = { viewModel.updateAddressInfo(state = it) },
        onZipCodeChange = { viewModel.updateAddressInfo(zipCode = it) },
        onReferenceChange = { viewModel.updateAddressInfo(reference = it) },
        onPlaceOrder = { viewModel.placeOrder() }
    )
}

@Composable
fun CheckoutAddressScreenContent(
    uiState: CheckoutUiState,
    onBackClick: () -> Unit,
    onStreetChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onStateChange: (String) -> Unit,
    onZipCodeChange: (String) -> Unit,
    onReferenceChange: (String) -> Unit,
    onPlaceOrder: () -> Unit
) {
    val address = uiState.addressInfo

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1E293B),
        unfocusedTextColor = Color(0xFF1E293B),
        focusedLabelColor = Color(0xFF4F46E5),
        unfocusedLabelColor = Color(0xFF64748B),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = Color(0xFF4F46E5),
        unfocusedBorderColor = Color(0xFFE2E8F0)
    )

    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AdminHeader(
                title = "Envío",
                subtitle = "Dirección de entrega",
                onBackClick = onBackClick,
                showProfile = false,
                showCart = false
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Step indicator
                CheckoutStepIndicator(currentStep = 2)
                
                Spacer(modifier = Modifier.height(8.dp))

                // Section header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFE0E7FF),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Default.LocalShipping, null, tint = Color(0xFF4F46E5), modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "¿A dónde enviamos?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "Ingresa la dirección de entrega",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = address.street,
                    onValueChange = onStreetChange,
                    label = { Text("Calle y número") },
                    leadingIcon = { Icon(Icons.Default.Home, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                OutlinedTextField(
                    value = address.city,
                    onValueChange = onCityChange,
                    label = { Text("Ciudad") },
                    leadingIcon = { Icon(Icons.Default.LocationCity, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = address.state,
                        onValueChange = onStateChange,
                        label = { Text("Estado") },
                        leadingIcon = { Icon(Icons.Default.Map, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = address.zipCode,
                        onValueChange = onZipCodeChange,
                        label = { Text("C.P.") },
                        leadingIcon = { Icon(Icons.Default.MarkunreadMailbox, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = address.reference ?: "",
                    onValueChange = onReferenceChange,
                    label = { Text("Referencias (Opcional)") },
                    leadingIcon = { Icon(Icons.Default.NoteAlt, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors
                )

                if (uiState.error != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF2F2)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(shape = CircleShape, color = Color(0xFFEF4444), modifier = Modifier.size(24.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = uiState.error,
                                color = Color(0xFFDC2626),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onPlaceOrder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4F46E5),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                    enabled = address.street.isNotBlank() && address.city.isNotBlank() && address.state.isNotBlank() && address.zipCode.isNotBlank() && !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.LocalShipping, null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "FINALIZAR PEDIDO",
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutAddressScreenPreview() {
    CheckoutAddressScreenContent(
        uiState = CheckoutUiState(addressInfo = AddressInfo()),
        onBackClick = {},
        onStreetChange = {},
        onCityChange = {},
        onStateChange = {},
        onZipCodeChange = {},
        onReferenceChange = {},
        onPlaceOrder = {}
    )
}
