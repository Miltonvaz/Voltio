package com.miltonvaz.voltio_1.features.products.presentation.components

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.miltonvaz.voltio_1.features.products.domain.entities.Product

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                if (!product.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color.LightGray
                    )
                }
                
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.8f)
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp).size(16.dp),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("4.8", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
            }

            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${product.price}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0xFF4F46E5)
                )
                
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFE0E7FF), CircleShape)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
