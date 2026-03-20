package com.miltonvaz.voltio_1.features.orders.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.AddressInfo
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CheckoutUiState
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.CheckoutViewModel
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader

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
        focusedLabelColor = Color(0xFF455E91),
        unfocusedLabelColor = Color(0xFF64748B),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = Color(0xFF455E91),
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
                showProfile = true,
                showCart = false
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "¿A dónde enviamos?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B)
                )

                OutlinedTextField(
                    value = address.street,
                    onValueChange = onStreetChange,
                    label = { Text("Calle y número") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                OutlinedTextField(
                    value = address.city,
                    onValueChange = onCityChange,
                    label = { Text("Ciudad") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = address.state,
                        onValueChange = onStateChange,
                        label = { Text("Estado") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = address.zipCode,
                        onValueChange = onZipCodeChange,
                        label = { Text("C.P.") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = address.reference ?: "",
                    onValueChange = onReferenceChange,
                    label = { Text("Referencias (Opcional)") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = textFieldColors
                )

                if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onPlaceOrder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF455E91),
                        contentColor = Color.White
                    ),
                    enabled = address.street.isNotBlank() && address.city.isNotBlank() && address.state.isNotBlank() && address.zipCode.isNotBlank() && !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
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
