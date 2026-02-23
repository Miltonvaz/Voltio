package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.R
import com.miltonvaz.voltio_1.features.products.domain.entities.Product

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(160.dp).padding(8.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.FavoriteBorder, null, modifier = Modifier.align(Alignment.TopEnd).size(20.dp), tint = Color.Gray)
            }

            Box(modifier = Modifier.height(100.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.voltio), contentDescription = null, modifier = Modifier.size(80.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("4.8", fontSize = 10.sp, color = Color.Gray)
            }

            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, color = Color(0xFF1A1C2E))

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "$${product.price}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0066FF))
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(28.dp).background(Color(0xFFE0E7FF), CircleShape)
                ) {
                    Icon(Icons.Default.ShoppingCart, null, tint = Color(0xFF0066FF), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}