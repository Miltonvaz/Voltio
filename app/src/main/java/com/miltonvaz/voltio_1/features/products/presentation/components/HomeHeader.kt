package com.miltonvaz.voltio_1.features.products.presentation.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.R
import com.miltonvaz.voltio_1.core.ui.theme.bodyFontFamily

@Composable
fun HomeHeader(
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit = {},
    onFavClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFCED9ED),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.voltio),
                    contentDescription = "Logo",
                    modifier = Modifier.size(32.dp)
                )

                Row {
                    IconButton(onClick = onFavClick) {
                        Icon(Icons.Default.FavoriteBorder, null, tint = Color(0xFF1A1C2E))
                    }
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, null, tint = Color(0xFF1A1C2E))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { onSearchClick() },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Buscar...", color = Color.Gray, fontFamily = bodyFontFamily)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF1A1C2E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Suchiapa, Chiapas",
                    fontSize = 12.sp,
                    color = Color(0xFF1A1C2E),
                    fontFamily = bodyFontFamily
                )
            }
        }
    }
}