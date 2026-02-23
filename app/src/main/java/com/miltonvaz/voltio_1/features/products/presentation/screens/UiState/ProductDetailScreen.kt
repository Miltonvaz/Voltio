package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.miltonvaz.voltio_1.features.products.domain.entities.Product
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader

@Composable
fun ProductDetailScreen(
    product: Product,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFDDE7FF)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            AdminHeader(
                title = product.name,
                subtitle = "Microcontroladores"
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = product.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "$${product.price}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0066FF),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(text = "SKU: ${product.sku}", color = Color.Gray, fontSize = 14.sp)
                Text(text = "${product.stock} Piezas disponibles", color = Color.Gray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailCategoryItem("M-Control", Icons.Default.Memory, true)
                    DetailCategoryItem("Sensores", Icons.Default.SettingsInputAntenna, false)
                    DetailCategoryItem("Componentes", Icons.Default.ToggleOn, false)
                    DetailCategoryItem("Robótica", Icons.Default.PrecisionManufacturing, false)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = product.description,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun DetailCategoryItem(label: String, icon: ImageVector, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) Color(0xFFDDE7FF) else Color(0xFFF8F9FA)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF0066FF) else Color.LightGray,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}