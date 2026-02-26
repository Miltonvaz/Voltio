package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
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

@Composable
fun DetailHeaderClient(
    productName: String,
    productCategory: String,
    categoryIconRes: Int,
    onNavigateBack: () -> Unit,
    onFavClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFCCDAFF),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Image(
                            painter = painterResource(id = R.drawable.flecha),
                            contentDescription = "Regresar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.voltio),
                        contentDescription = "Logo",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Row {
                    IconButton(onClick = onFavClick) {
                        Image(
                            painter = painterResource(id = R.drawable.corazon),
                            contentDescription = "Favorito",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color(0xFF1A1C2E), modifier = Modifier.size(24.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(productName, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF1A1C2E))
                    Text(productCategory, fontSize = 13.sp, color = Color.Gray)
                }
                Image(
                    painter = painterResource(id = categoryIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}