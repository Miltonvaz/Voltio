package com.miltonvaz.voltio1.features.products.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.miltonvaz.voltio1.features.products.domain.entities.Product
import java.util.Locale

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit = {},
    onCompanyClick: ((Int) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F7FA)),
                contentAlignment = Alignment.Center
            ) {
                if (!product.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp),
                        tint = Color(0xFFD1D9E6)
                    )
                }

                // Badge disponibilidad
                if (product.stock > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF10B981)
                    ) {
                        Text(
                            text = "Disponible",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Favorito
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(16.dp),
                        tint = Color(0xFFB0B8C9)
                    )
                }
            }

            // Información del producto
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ⭐ Rating dinámico
                val rating = product.rating ?: 4.5f
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            Icons.Default.Star,
                            null,
                            tint = if (index < rating.toInt()) Color(0xFFFFC107) else Color(0xFFE2E8F0),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format(Locale.US, "%.1f", rating),
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Nombre de empresa
                if (product.companyName != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            product.companyId?.let { onCompanyClick?.invoke(it) }
                        }
                    ) {
                        Surface(
                            modifier = Modifier.size(16.dp),
                            shape = CircleShape,
                            color = Color(0xFFF1F5F9)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (!product.companyLogoUrl.isNullOrBlank()) {
                                    AsyncImage(
                                        model = product.companyLogoUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Store,
                                        null,
                                        tint = Color(0xFF4F46E5),
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = product.companyName!!,
                            fontSize = 10.sp,
                            color = Color(0xFF4F46E5),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 💲 Precio en azul
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", product.price)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = Color(0xFF2563EB)
                    )

                    // 🛒 Botón circular con ícono de carrito
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onAddToCart() },
                        shape = CircleShape,
                        color = Color(0xFF2563EB),
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Agregar al carrito",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}