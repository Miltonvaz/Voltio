package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.R

@Composable
fun BannerCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFCC0050), Color(0xFF6200EE), Color(0xFF1A00CC))
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 16.dp, bottom = 16.dp, end = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ── Texto izquierda ────────────────────────────────
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "ESP32",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp
                    )
                    Text(
                        "Microcontroladores",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "ESP32 DevKit: Potente\nmicrocontrolador con Wi-Fi y\nBluetooth integrados",
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                // ── Imagen derecha ─────────────────────────────────
                Image(
                    painter = painterResource(id = R.drawable.esp32),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = 10.dp)
                )
            }
        }
    }
}