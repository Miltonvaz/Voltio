package com.miltonvaz.voltio1.features.auth.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.UserDto
import com.miltonvaz.voltio1.features.auth.presentation.viewmodel.ProfileViewModel
import com.miltonvaz.voltio1.features.products.presentation.components.BottomNavBarClient

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { BottomNavBarClient(navController = navController, selectedIndex = 3) },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF4F46E5)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(shape = CircleShape, color = Color(0xFFFFEBEB), modifier = Modifier.size(72.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.ErrorOutline, null, tint = Color(0xFFEF4444), modifier = Modifier.size(36.dp))
                            }
                        }
                        Text("No se pudo cargar el perfil", color = Color(0xFF64748B), fontSize = 16.sp)
                        Button(
                            onClick = { viewModel.loadProfile() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                            shape = RoundedCornerShape(14.dp)
                        ) { Text("Reintentar") }
                    }
                }
                uiState.user != null -> {
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { visible = true }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 12 }
                    ) {
                        ProfileContent(user = uiState.user!!)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(user: UserDto) {
    val fullName = buildString {
        append(user.name)
        user.secondname?.let { append(" $it") }
        append(" ${user.lastname}")
        user.secondlastname?.let { append(" $it") }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Avatar + nombre centrados
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar con borde gradiente
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))
                                )
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8EEFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.first().uppercaseChar().toString(),
                            color = Color(0xFF4F46E5),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    // Badge de rol
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF4F46E5),
                        modifier = Modifier
                            .size(26.dp)
                            .border(2.dp, Color(0xFFF8FAFC), CircleShape)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = fullName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user.email,
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Normal
                )

                if (!user.phone.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = user.phone,
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        // Sección info de cuenta
        item {
            SectionLabel("Información de cuenta")
            SettingsCard {
                SettingsRow(
                    icon = Icons.Default.Person,
                    iconBg = Color(0xFFE8EEFF),
                    iconTint = Color(0xFF4F46E5),
                    label = "Nombre",
                    value = fullName
                )
                RowDivider()
                SettingsRow(
                    icon = Icons.Default.Email,
                    iconBg = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF10B981),
                    label = "Correo",
                    value = user.email
                )
                RowDivider()
                SettingsRow(
                    icon = Icons.Default.Phone,
                    iconBg = Color(0xFFFFF3E0),
                    iconTint = Color(0xFFF59E0B),
                    label = "Teléfono",
                    value = user.phone ?: "No registrado"
                )
            }
        }

        // Sección detalles
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionLabel("Detalles")
            SettingsCard {
                SettingsRow(
                    icon = Icons.Default.Badge,
                    iconBg = Color(0xFFF3E8FF),
                    iconTint = Color(0xFF7C3AED),
                    label = "Rol",
                    value = user.role.replaceFirstChar { it.uppercase() }
                )
                RowDivider()
                SettingsRow(
                    icon = Icons.Default.CalendarMonth,
                    iconBg = Color(0xFFE8EEFF),
                    iconTint = Color(0xFF4F46E5),
                    label = "Miembro desde",
                    value = user.created_at.take(10)
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF94A3B8),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        content = { Column(modifier = Modifier.padding(horizontal = 4.dp), content = content) }
    )
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(modifier = Modifier.size(40.dp), shape = RoundedCornerShape(12.dp), color = iconBg) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B)
        )
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 68.dp, end = 12.dp),
        color = Color(0xFFF1F5F9),
        thickness = 1.dp
    )
}
