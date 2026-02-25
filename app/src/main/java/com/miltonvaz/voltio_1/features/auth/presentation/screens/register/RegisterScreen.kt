package com.miltonvaz.voltio_1.features.auth.presentation.screens.register

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio_1.R
import com.miltonvaz.voltio_1.core.ui.components.CustomTextField
import com.miltonvaz.voltio_1.core.ui.components.PrimaryButton
import com.miltonvaz.voltio_1.core.ui.components.SocialButton
import com.miltonvaz.voltio_1.core.ui.components.TextDivider
import com.miltonvaz.voltio_1.core.ui.theme.bodyFontFamily
import com.miltonvaz.voltio_1.core.ui.theme.displayFontFamily
import com.miltonvaz.voltio_1.features.auth.presentation.components.LoginTopBar
import com.miltonvaz.voltio_1.features.auth.presentation.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.voltio),
                        contentDescription = "Voltio Logo",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Voltio",
                        fontFamily = bodyFontFamily,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C2E)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
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
                            text = "¡Regístrate gratis!",
                            fontFamily = displayFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C2E)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Voltio - Potencia electrónica",
                            fontFamily = displayFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF616161),
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        CustomTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nombre",
                            keyboardType = KeyboardType.Text
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CustomTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = "Apellidos",
                            keyboardType = KeyboardType.Text
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CustomTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Teléfono",
                            keyboardType = KeyboardType.Phone
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CustomTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Correo Electrónico",
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

                        Spacer(modifier = Modifier.height(12.dp))

                        CustomTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Confirma tu contraseña",
                            isPassword = true,
                            keyboardType = KeyboardType.Password
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        PrimaryButton(
                            text = "Registrarse",
                            onClick = {
                                viewModel.register(
                                    name = name,
                                    lastName = lastName,
                                    email = email,
                                    pass = password,
                                    phone = phone
                                )
                            },
                            enabled = !uiState.isLoading &&
                                    name.isNotBlank() &&
                                    lastName.isNotBlank() &&
                                    email.isNotBlank() &&
                                    password.isNotBlank() &&
                                    password == confirmPassword
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        TextDivider(text = "O inicia sesión con")

                        Spacer(modifier = Modifier.height(20.dp))

                        SocialButton(
                            text = "Google",
                            iconRes = R.drawable.google,
                            onClick = { }
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF7E8FBC)
                )
            }

            if (uiState.error != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor = Color(0xFFB8C5E0),
                    contentColor = Color(0xFF1A1C2E),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK", color = Color(0xFF1A1C2E))
                        }
                    }
                ) {
                    Text(uiState.error ?: "Error desconocido")
                }
            }
        }
    }
}