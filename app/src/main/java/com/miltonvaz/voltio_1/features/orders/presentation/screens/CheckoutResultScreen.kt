package com.miltonvaz.voltio_1.features.orders.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckoutResultScreen(
    isSuccess: Boolean,
    onFinish: () -> Unit
) {
    val themeColor = if (isSuccess) Color(0xFF4F46E5) else Color(0xFFEF4444)
    val lightThemeColor = if (isSuccess) Color(0xFFE0E7FF) else Color(0xFFFEE2E2)
    val icon = if (isSuccess) Icons.Default.Check else Icons.Default.Close
    val title = if (isSuccess) "¡Compra Exitosa!" else "Hubo un problema"
    val subtitle = if (isSuccess) "Tu pedido ya está en camino" else "No se pudo procesar el pago"
    val message = if (isSuccess) {
        "Hemos recibido tu pedido correctamente. Recibirás un correo con los detalles del seguimiento muy pronto."
    } else {
        "Lo sentimos, pero no pudimos completar tu pedido. Por favor, verifica tu método de pago e inténtalo de nuevo."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0E7FF), Color(0xFFF8FAFC))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(140.dp),
                    shape = CircleShape,
                    color = lightThemeColor.copy(alpha = 0.5f)
                ) {}
                Surface(
                    modifier = Modifier.size(110.dp),
                    shape = CircleShape,
                    color = lightThemeColor
                ) {}
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = themeColor,
                    shadowElevation = 8.dp
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E1B4B),
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = subtitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColor,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSuccess) Color(0xFFA9C1FF) else Color(0xFF1E1B4B),
                    contentColor = if (isSuccess) Color(0xFF1E1B4B) else Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "VOLVER AL INICIO",
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutSuccessPreview() {
    CheckoutResultScreen(isSuccess = true, onFinish = {})
}

@Preview(showBackground = true)
@Composable
fun CheckoutErrorPreview() {
    CheckoutResultScreen(isSuccess = false, onFinish = {})
}
