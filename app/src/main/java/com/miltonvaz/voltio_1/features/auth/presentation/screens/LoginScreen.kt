package com.miltonvaz.voltio_1.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.core.ui.components.CustomTextField
import com.miltonvaz.voltio_1.core.ui.components.PrimaryButton
import com.miltonvaz.voltio_1.core.ui.components.SocialButton
import com.miltonvaz.voltio_1.core.ui.components.TextDivider
import com.miltonvaz.voltio_1.core.ui.theme.displayFontFamily
import com.miltonvaz.voltio_1.R
import com.miltonvaz.voltio_1.features.auth.domain.entities.Auth
import com.miltonvaz.voltio_1.features.auth.presentation.viewmodel.LoginViewModel
import com.miltonvaz.voltio_1.features.products.presentation.components.AdminHeader

@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: (Auth?) -> Unit = {}
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onLoginSuccess(uiState.user)
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8FAFC))
        ) {
            // Header unificado tal cual la imagen
            AdminHeader(
                title = "Bienvenido",
                subtitle = "Inicia sesión para continuar",
                onBackClick = null, // No hay atrás en el login principal
                showProfile = false // No hay perfil antes de loguear
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp, bottom = 40.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "¡Hola de nuevo!",
                        fontFamily = displayFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C2E)
                    )

                    Text(
                        text = "Voltio - Potencia electrónica",
                        fontFamily = displayFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF616161),
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Correo electrónico",
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Contraseña",
                        isPassword = true,
                        keyboardType = KeyboardType.Password
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "INGRESAR",
                        onClick = { viewModel.login(email, password) },
                        enabled = !uiState.isLoading && email.isNotBlank() && password.isNotBlank()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextDivider(text = "O inicia sesión con")

                    Spacer(modifier = Modifier.height(24.dp))

                    SocialButton(
                        text = "Google",
                        iconRes = R.drawable.google,
                        onClick = { }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TextButton(
                        onClick = onRegisterClick,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("¿No tienes cuenta? Regístrate", color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF4F46E5))
            }
        }

        if (uiState.error != null) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFF1E1B4B),
                contentColor = Color.White,
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK", color = Color.White)
                    }
                }
            ) {
                Text(uiState.error ?: "Error desconocido")
            }
        }
    }
}
