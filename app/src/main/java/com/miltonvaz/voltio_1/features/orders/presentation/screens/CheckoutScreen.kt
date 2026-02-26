package com.miltonvaz.voltio_1.features.orders.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CardInfo
import com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState.CheckoutUiState
import com.miltonvaz.voltio_1.features.orders.presentation.viewmodel.CheckoutViewModel
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader

@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CheckoutScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onContinueClick = onContinueClick,
        onCardInfoChange = { number, name, expiry, cvv ->
            viewModel.updateCardInfo(number, name, expiry, cvv)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreenContent(
    uiState: CheckoutUiState,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onCardInfoChange: (String, String, String, String) -> Unit
) {
    val card = uiState.cardInfo

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
                .verticalScroll(rememberScrollState())
        ) {
            // Header con estilo Voltio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))
                        )
                    )
                    .padding(bottom = 16.dp)
            ) {
                Column {
                    IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF1E1B4B))
                    }
                    AdminHeader(
                        title = "Método de Pago",
                        subtitle = "Seguridad en tu transacción"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Información de Tarjeta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B)
                )

                OutlinedTextField(
                    value = card.name,
                    onValueChange = { onCardInfoChange(card.number, it, card.expiry, card.cvv) },
                    label = { Text("Nombre en la tarjeta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                OutlinedTextField(
                    value = card.number,
                    onValueChange = { if (it.length <= 16) onCardInfoChange(it, card.name, card.expiry, card.cvv) },
                    label = { Text("Número de tarjeta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = textFieldColors,
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = card.expiry,
                        onValueChange = { if (it.length <= 5) onCardInfoChange(card.number, card.name, it, card.cvv) },
                        label = { Text("MM/AA") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = card.cvv,
                        onValueChange = { if (it.length <= 3) onCardInfoChange(card.number, card.name, card.expiry, it) },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA9C1FF),
                        contentColor = Color(0xFF1E293B)
                    ),
                    enabled = card.name.isNotBlank() && card.number.length == 16 && card.expiry.length == 5 && card.cvv.length == 3
                ) {
                    Text(
                        text = "CONTINUAR A DIRECCIÓN",
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    CheckoutScreenContent(
        uiState = CheckoutUiState(cardInfo = CardInfo()),
        onBackClick = {},
        onContinueClick = {},
        onCardInfoChange = { _, _, _, _ -> }
    )
}
