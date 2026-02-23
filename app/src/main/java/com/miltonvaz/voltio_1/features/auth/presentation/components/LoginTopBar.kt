package com.miltonvaz.voltio_1.features.auth.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.core.ui.theme.displayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopBar(
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
    isLoginMode: Boolean = true
) {
    TopAppBar(
        title = {
            Text(
                text = "¿No tienes una cuenta?",
                fontFamily = displayFontFamily,
                fontSize = 14.sp,
                color = Color(0xFF424242)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color(0xFF424242)
                )
            }
        },
        actions = {
            Button(
                onClick = onRegisterClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7E8FBC),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "Registrarte",
                    fontFamily = displayFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFCED9ED)
        )
    )
}
