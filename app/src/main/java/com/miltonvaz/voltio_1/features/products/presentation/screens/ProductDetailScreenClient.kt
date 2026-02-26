package com.miltonvaz.voltio_1.features.products.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.miltonvaz.voltio_1.features.products.presentation.components.BottomNavBarClient
import com.miltonvaz.voltio_1.features.products.presentation.components.getProductImage

@Composable
fun ProductDetailScreenClient(
    product: Product,
    onNavigateBack: () -> Unit
) {
    var quantity by remember { mutableStateOf(1) }
    var selectedNavIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavBarClient(
                selectedIndex = selectedNavIndex,
                onItemSelected = { selectedNavIndex = it }
            )
        },
        containerColor = Color(0xFFDDE8FF)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color(0xFF1A1C2E)
                    )
                }
                Row {
                    IconButton(onClick = {}) {
                        Image(
                            painter = painterResource(id = R.drawable.corazon),
                            contentDescription = "Favorito",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = {}) {
                        Image(
                            painter = painterResource(id = R.drawable.carrito),
                            contentDescription = "Carrito",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(product.name, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF1A1C2E))
                    Text("Microcontroladores", fontSize = 13.sp, color = Color.Gray)
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.microcontrolador),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) {
                        Image(
                            painter = painterResource(id = R.drawable.cautin),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
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

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailCategoryItemClient("Microcontroladores", R.drawable.microcontrolador, true)
                DetailCategoryItemClient("Sensores", R.drawable.sensor, false)
                DetailCategoryItemClient("Componentes", R.drawable.componentes, false)
                DetailCategoryItemClient("Robótica", R.drawable.brazo_robotico, false)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = product.description,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color(0xFF4A5568),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }, modifier = Modifier.size(32.dp)) {
                        Text("−", fontSize = 20.sp, color = Color(0xFF1A1C2E), fontWeight = FontWeight.Bold)
                    }
                    Text("$quantity", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C2E), modifier = Modifier.padding(horizontal = 12.dp))
                    IconButton(onClick = { quantity++ }, modifier = Modifier.size(32.dp)) {
                        Text("+", fontSize = 20.sp, color = Color(0xFF1A1C2E), fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1C2E)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.carrito),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Añadir al carrito", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DetailCategoryItemClient(label: String, iconRes: Int, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isSelected) Color(0xFFDDE8FF) else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )
        }
        Text(text = label, fontSize = 9.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
    }
}