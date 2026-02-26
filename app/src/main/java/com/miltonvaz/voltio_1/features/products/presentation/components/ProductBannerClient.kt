package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.R
import com.miltonvaz.voltio_1.features.products.domain.entities.Product

@Composable
fun ProductBannerClient(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFFFFFF))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text("4.8", fontSize = 13.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("$${product.price}", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Color(0xFF1A1C2E))
                Spacer(modifier = Modifier.height(4.dp))
                Text("SKU: ${product.sku}", fontSize = 12.sp, color = Color.Gray)
                Text("${product.stock} Piezas disponibles", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.corazon),
                    contentDescription = "Favorito",
                    modifier = Modifier.size(22.dp)
                )
            }
            Image(
                painter = painterResource(id = getProductImage(product.name)),
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (index == 0) 16.dp else 8.dp, 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (index == 0) Color(0xFF1A1C2E) else Color(0xFFCBD5E1))
                )
            }
        }
    }
}