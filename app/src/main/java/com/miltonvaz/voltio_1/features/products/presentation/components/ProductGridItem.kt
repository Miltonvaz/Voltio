package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.R
import com.miltonvaz.voltio_1.features.products.domain.entities.Product

fun getProductImage(name: String): Int {
    val lower = name.lowercase()
    return when {
        lower.contains("cautin") || lower.contains("cautín") -> R.drawable.cautin
        lower.contains("capacitor") -> R.drawable.capacitor
        lower.contains("estaño") || lower.contains("estano") -> R.drawable.estano
        lower.contains("raspberry") -> R.drawable.raspberry
        lower.contains("resistencia") -> R.drawable.resistencias
        lower.contains("esp32") || lower.contains("esp") -> R.drawable.esp32
        lower.contains("protoboard") || lower.contains("proto") -> R.drawable.protoboard
        lower.contains("led") -> R.drawable.leds
        else -> R.drawable.voltio
    }
}

@Composable
fun ProductGridItem(
    product: Product,
    onFavoriteClick: () -> Unit,
    onCartClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(6.dp)) {

            // ── Imagen + favorito ──────────────────────────────────
            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    Image(
                        painter = painterResource(id = getProductImage(product.name)),
                        contentDescription = product.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                    )
                }
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ── Estrellas ──────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(4) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(8.dp)
                    )
                }
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(8.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text("4.8", fontSize = 8.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(2.dp))

            // ── Nombre ─────────────────────────────────────────────
            Text(
                product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF1A1C2E)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── Precio + carrito ───────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFF1A6BFF)
                )
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color(0xFFCCDAFF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onCartClick) {
                        Image(
                            painter = painterResource(id = R.drawable.carrito),
                            contentDescription = "Carrito",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}