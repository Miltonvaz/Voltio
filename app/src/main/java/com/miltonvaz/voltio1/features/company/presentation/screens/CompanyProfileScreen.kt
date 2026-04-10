package com.miltonvaz.voltio1.features.company.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.miltonvaz.voltio1.features.company.presentation.viewmodel.CompanyViewModel
import com.miltonvaz.voltio1.features.products.domain.entities.Product
import java.util.Locale
import android.location.Geocoder
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

@Composable
fun CompanyProfileScreen(
    companyId: Int,
    onNavigateBack: () -> Unit,
    onProductClick: (Int) -> Unit
) {
    val viewModel: CompanyViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(companyId) {
        viewModel.loadCompany(companyId)
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4F46E5))
            }
        } else if (uiState.company != null) {
            val company = uiState.company!!
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Header con gradiente y logo
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFE8EEFF), Color(0xFFF0F4FF))
                                )
                            )
                            .statusBarsPadding()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Top bar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = onNavigateBack) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF4F46E5).copy(alpha = 0.1f),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = null,
                                                tint = Color(0xFF4F46E5),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                                
                                Text(
                                    "Tienda",
                                    color = Color(0xFF1E293B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.size(40.dp))
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Logo de empresa
                            Surface(
                                modifier = Modifier.size(100.dp),
                                shape = CircleShape,
                                color = Color.White,
                                shadowElevation = 8.dp
                            ) {
                                if (!company.logoUrl.isNullOrBlank()) {
                                    AsyncImage(
                                        model = company.logoUrl,
                                        contentDescription = company.commercialName,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Store,
                                            contentDescription = null,
                                            tint = Color(0xFF4F46E5),
                                            modifier = Modifier.size(48.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = company.commercialName,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }
                }

                // Info cards
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CompanyStatCard(
                            icon = Icons.Default.Inventory2,
                            value = "${uiState.products.size}",
                            label = "Productos",
                            color = Color(0xFF4F46E5),
                            modifier = Modifier.weight(1f)
                        )
                        CompanyStatCard(
                            icon = Icons.Default.Star,
                            value = "4.8",
                            label = "Valoración",
                            color = Color(0xFFF59E0B),
                            modifier = Modifier.weight(1f)
                        )
                        CompanyStatCard(
                            icon = Icons.Default.LocalShipping,
                            value = "Sí",
                            label = "Envíos",
                            color = Color(0xFF10B981),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Información de contacto
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Información de contacto",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (company.contactPhone.isNotBlank()) {
                                ContactInfoRow(
                                    icon = Icons.Default.Phone,
                                    text = company.contactPhone
                                )
                            }
                            if (company.contactEmail.isNotBlank()) {
                                if (company.contactPhone.isNotBlank()) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        color = Color(0xFFF1F5F9)
                                    )
                                }
                                ContactInfoRow(
                                    icon = Icons.Default.Email,
                                    text = company.contactEmail
                                )
                            }
                            if (company.address.isNotBlank()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    color = Color(0xFFF1F5F9)
                                )
                                ContactInfoRow(
                                    icon = Icons.Default.LocationOn,
                                    text = company.address
                                )
                            }
                        }
                    }
                }

                // Mapa de ubicación
                val hasCoordinates = company.latitude != 0.0 || company.longitude != 0.0
                val hasAddress = company.address.isNotBlank()

                if (hasCoordinates || hasAddress) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Ubicación",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val context = LocalContext.current
                        var mapLocation by remember { mutableStateOf<LatLng?>(null) }
                        var geocodingDone by remember { mutableStateOf(false) }

                        LaunchedEffect(company) {
                            if (hasCoordinates) {
                                mapLocation = LatLng(company.latitude, company.longitude)
                                geocodingDone = true
                            } else {
                                withContext(Dispatchers.IO) {
                                    // Try Android Geocoder first
                                    try {
                                        if (Geocoder.isPresent()) {
                                            @Suppress("DEPRECATION")
                                            val results = Geocoder(context, Locale.getDefault())
                                                .getFromLocationName(company.address, 1)
                                            if (!results.isNullOrEmpty()) {
                                                mapLocation = LatLng(results[0].latitude, results[0].longitude)
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.e("CompanyProfile", "Geocoder failed: ${e.message}")
                                    }

                                    // Fallback: Google Geocoding REST API
                                    if (mapLocation == null) {
                                        try {
                                            val apiKey = com.miltonvaz.voltio1.BuildConfig.MAPS_API_KEY
                                            val encoded = URLEncoder.encode(company.address, "UTF-8")
                                            val url = "https://maps.googleapis.com/maps/api/geocode/json?address=$encoded&key=$apiKey"
                                            val response = URL(url).readText()
                                            val json = JSONObject(response)
                                            val results = json.getJSONArray("results")
                                            if (results.length() > 0) {
                                                val location = results.getJSONObject(0)
                                                    .getJSONObject("geometry")
                                                    .getJSONObject("location")
                                                mapLocation = LatLng(
                                                    location.getDouble("lat"),
                                                    location.getDouble("lng")
                                                )
                                            }
                                            Log.d("CompanyProfile", "Geocoding API status: ${json.optString("status")}")
                                        } catch (e: Exception) {
                                            Log.e("CompanyProfile", "Geocoding API failed: ${e.message}")
                                        }
                                    }
                                    geocodingDone = true
                                }
                            }
                        }

                        if (mapLocation != null) {
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(mapLocation!!, 15f)
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .height(200.dp),
                                shape = RoundedCornerShape(16.dp),
                                shadowElevation = 2.dp
                            ) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraPositionState
                                ) {
                                    Marker(
                                        state = MarkerState(position = mapLocation!!),
                                        title = company.commercialName,
                                        snippet = company.address
                                    )
                                }
                            }
                        } else if (!geocodingDone) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .height(200.dp),
                                shape = RoundedCornerShape(16.dp),
                                shadowElevation = 2.dp,
                                color = Color(0xFFF1F5F9)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(
                                        color = Color(0xFF4F46E5),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Productos de la empresa
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Productos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E293B)
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8EEFF)
                        ) {
                            Text(
                                text = "${uiState.products.size} items",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4F46E5)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Grid de productos
                val chunkedProducts = uiState.products.chunked(2)
                itemsIndexed(chunkedProducts) { rowIndex, rowProducts ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowProducts.forEach { product ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) { visible = true }

                            AnimatedVisibility(
                                visible = visible,
                                modifier = Modifier.weight(1f),
                                enter = slideInVertically(
                                    initialOffsetY = { 100 },
                                    animationSpec = tween(durationMillis = 500, delayMillis = rowIndex * 100)
                                ) + fadeIn()
                            ) {
                                CompanyProductCard(
                                    product = product,
                                    onClick = { onProductClick(product.id) }
                                )
                            }
                        }
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ErrorOutline, null, tint = Color(0xFFEF4444), modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(uiState.error ?: "Error", color = Color(0xFF64748B))
                }
            }
        }
    }
}

@Composable
private fun CompanyStatCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color(0xFF1E293B)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ContactInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF1F5F9),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color(0xFF4F46E5), modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF475569),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CompanyProductCard(
    product: Product,
    onClick: () -> Unit
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
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
                        Icons.Default.ShoppingCart,
                        null,
                        modifier = Modifier.size(40.dp),
                        tint = Color(0xFFD1D9E6)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$${String.format(Locale.US, "%.2f", product.price)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0xFF4F46E5)
                )
            }
        }
    }
}
