package com.miltonvaz.voltio1.features.auth.presentation.screens.register

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.miltonvaz.voltio1.core.ui.components.CustomTextField
import com.miltonvaz.voltio1.core.ui.components.PrimaryButton
import com.miltonvaz.voltio1.core.ui.components.SocialButton
import com.miltonvaz.voltio1.core.ui.components.TextDivider
import com.miltonvaz.voltio1.core.ui.theme.bodyFontFamily
import com.miltonvaz.voltio1.core.ui.theme.displayFontFamily
import com.miltonvaz.voltio1.features.auth.presentation.components.LoginTopBar
import com.miltonvaz.voltio1.features.auth.presentation.viewmodel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import com.miltonvaz.voltio1.R
@SuppressLint("MissingPermission")
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedRole by remember { mutableStateOf("user") }

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Campos Empresa
    var commercialName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var isUpdatingFromMap by remember { mutableStateOf(false) }

    // Campos Repartidor
    var vehicle by remember { mutableStateOf("moto") }
    var plates by remember { mutableStateOf("") }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(16.6248, -93.1025), 15f)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    selectedLocation = latLng
                    isUpdatingFromMap = true
                    scope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                        updateAddressFromLocation(context, latLng) { newAddress ->
                            address = newAddress
                            isUpdatingFromMap = false
                        }
                    }
                }
            }
        }
    }

    // Efecto para buscar ubicación por texto (Forward Geocoding)
    LaunchedEffect(address) {
        if (!isUpdatingFromMap && address.length > 5 && selectedRole == "company") {
            delay(1500) // Debounce para no saturar el servicio
            updateLocationFromAddress(context, address) { latLng ->
                selectedLocation = latLng
                scope.launch {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                }
            }
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            LoginTopBar(
                onBackClick = onBackClick,
                onRegisterClick = onLoginClick,
                isLoginMode = false
            )
        },
        containerColor = Color(0xFFCED9ED)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFCED9ED))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.voltio),
                        contentDescription = "Voltio Logo",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Voltio",
                        fontFamily = bodyFontFamily,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C2E)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp, bottom = 40.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "¡Únete a Voltio!",
                            fontFamily = displayFontFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C2E)
                        )

                        Text(
                            text = "Selecciona tu tipo de cuenta",
                            fontFamily = displayFontFamily,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val roles = listOf("user" to "Usuario", "company" to "Empresa", "delivery" to "Repartidor")
                            roles.forEach { (role, label) ->
                                RoleChip(
                                    label = label,
                                    selected = selectedRole == role,
                                    onClick = { if (!uiState.isGoogleLinked) selectedRole = role },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        if (uiState.isGoogleLinked) {
                            Surface(
                                color = Color(0xFFF0FDF4),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF16A34A))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Google vinculado correctamente", color = Color(0xFF166534), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        if (!uiState.isGoogleLinked) {
                            CustomTextField(value = name, onValueChange = { name = it }, label = "Nombre")
                            Spacer(modifier = Modifier.height(12.dp))
                            CustomTextField(value = lastName, onValueChange = { lastName = it }, label = "Apellidos")
                            Spacer(modifier = Modifier.height(12.dp))
                            CustomTextField(value = email, onValueChange = { email = it }, label = "Correo", keyboardType = KeyboardType.Email)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        
                        CustomTextField(value = phone, onValueChange = { phone = it }, label = "Teléfono", keyboardType = KeyboardType.Phone)
                        Spacer(modifier = Modifier.height(12.dp))

                        if (selectedRole == "company") {
                            // Logo upload section
                            Text("Logo de la Empresa", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C2E))
                            Spacer(modifier = Modifier.height(8.dp))

                            val logoCameraLauncher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.TakePicture()
                            ) { success -> viewModel.onLogoCameraResult(success) }

                            val logoGalleryLauncher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.GetContent()
                            ) { uri -> uri?.let { viewModel.onLogoGalleryResult(it) } }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFF8FAFC),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (viewModel.selectedLogoBytes != null) Color(0xFF10B981) else Color(0xFFE2E8F0)
                                )
                            ) {
                                if (viewModel.selectedLogoBytes != null) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = viewModel.selectedLogoBytes,
                                            contentDescription = "Logo preview",
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Store,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            tint = Color(0xFFB0B8C9)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            viewModel.logoName,
                                            fontSize = 12.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val uri = viewModel.createLogoUri()
                                        logoCameraLauncher.launch(uri)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF455E91))
                                ) {
                                    Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Cámara", fontSize = 13.sp)
                                }
                                OutlinedButton(
                                    onClick = { logoGalleryLauncher.launch("image/*") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF455E91))
                                ) {
                                    Icon(Icons.Default.CloudUpload, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Galería", fontSize = 13.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomTextField(value = commercialName, onValueChange = { commercialName = it }, label = "Nombre Comercial")
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            CustomTextField(
                                value = address, 
                                onValueChange = { address = it }, 
                                label = "Dirección Fiscal"
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Ubicación en el Mapa", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C2E))
                                IconButton(onClick = {
                                    val hasFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    val hasCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    
                                    if (hasFineLocation || hasCoarseLocation) {
                                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                            location?.let {
                                                val latLng = LatLng(it.latitude, it.longitude)
                                                selectedLocation = latLng
                                                isUpdatingFromMap = true
                                                scope.launch {
                                                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                                                    updateAddressFromLocation(context, latLng) { newAddress ->
                                                        address = newAddress
                                                        isUpdatingFromMap = false
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        permissionLauncher.launch(arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ))
                                    }
                                }) {
                                    Icon(Icons.Default.MyLocation, contentDescription = "Mi ubicación", tint = Color(0xFF455E91))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp))) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraPositionState,
                                    properties = MapProperties(isMyLocationEnabled = false),
                                    onMapLongClick = { latLng ->
                                        selectedLocation = latLng
                                        isUpdatingFromMap = true
                                        scope.launch {
                                            updateAddressFromLocation(context, latLng) { newAddress ->
                                                address = newAddress
                                                isUpdatingFromMap = false
                                            }
                                        }
                                    }
                                ) {
                                    selectedLocation?.let { Marker(state = MarkerState(position = it)) }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (selectedRole == "delivery") {
                            Text("Tipo de Vehículo", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                                listOf("moto", "auto", "bici").forEach { v ->
                                    FilterChip(
                                        selected = vehicle == v,
                                        onClick = { vehicle = v },
                                        label = { Text(v.uppercase()) }
                                    )
                                }
                            }
                            CustomTextField(value = plates, onValueChange = { plates = it }, label = "Placas (Opcional)")
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (!uiState.isGoogleLinked) {
                            CustomTextField(value = password, onValueChange = { password = it }, label = "Contraseña", isPassword = true)
                            Spacer(modifier = Modifier.height(12.dp))
                            CustomTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirmar Contraseña", isPassword = true)
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        PrimaryButton(
                            text = if (uiState.isGoogleLinked) "COMPLETAR REGISTRO" else "REGISTRARME",
                            onClick = {
                                when(selectedRole) {
                                    "user" -> viewModel.registerUser(name, lastName, email, password, phone)
                                    "company" -> {
                                        val lat = selectedLocation?.latitude ?: 0.0
                                        val lng = selectedLocation?.longitude ?: 0.0
                                        viewModel.registerCompany(name, lastName, email, password, phone, commercialName, address, lat, lng, uiState.googleIdToken)
                                    }
                                    "delivery" -> viewModel.registerDelivery(name, lastName, email, password, phone, vehicle, plates.ifBlank { null }, uiState.googleIdToken)
                                }
                            },
                            enabled = !uiState.isLoading && phone.isNotBlank() && (uiState.isGoogleLinked || (email.isNotBlank() && password == confirmPassword))
                        )

                        if (!uiState.isGoogleLinked) {
                            Spacer(modifier = Modifier.height(20.dp))
                            TextDivider(text = "O regístrate con")
                            Spacer(modifier = Modifier.height(20.dp))

                            SocialButton(
                                text = "Google",
                                iconRes = R.drawable.google,
                                onClick = { viewModel.onGoogleSignIn(context, selectedRole) }
                            )
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF455E91))
            }

            if (uiState.error != null) {
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                    containerColor = Color(0xFF1E1B4B),
                    action = { TextButton(onClick = { viewModel.clearError() }) { Text("OK", color = Color.White) } }
                ) { Text(uiState.error ?: "Error") }
            }
        }
    }
}

private suspend fun updateAddressFromLocation(context: Context, latLng: LatLng, onAddressFound: (String) -> Unit) {
    withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        onAddressFound(addresses[0].getAddressLine(0))
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        onAddressFound(addresses[0].getAddressLine(0))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private suspend fun updateLocationFromAddress(context: Context, address: String, onLocationFound: (LatLng) -> Unit) {
    withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocationName(address, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        onLocationFound(LatLng(addresses[0].latitude, addresses[0].longitude))
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(address, 1)
                if (!addresses.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        onLocationFound(LatLng(addresses[0].latitude, addresses[0].longitude))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun RoleChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Color(0xFF455E91) else Color(0xFFF1F5F9),
        contentColor = if (selected) Color.White else Color.Gray,
        modifier = modifier.height(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
