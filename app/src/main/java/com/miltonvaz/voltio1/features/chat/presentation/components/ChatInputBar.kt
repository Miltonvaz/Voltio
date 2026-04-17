package com.miltonvaz.voltio1.features.chat.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio1.features.chat.presentation.screens.UiState.ArchivoAdjunto

@Composable
fun ChatInputBar(
    texto: String,
    onTextoChange: (String) -> Unit,
    onEnviar: () -> Unit,
    onArchivoSeleccionado: (ArchivoAdjunto) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val cr = context.contentResolver
            val mimeType = cr.getType(it) ?: "application/octet-stream"
            val nombre = it.lastPathSegment ?: "archivo"
            val bytes = cr.openInputStream(it)?.readBytes() ?: return@let
            onArchivoSeleccionado(ArchivoAdjunto(bytes, nombre, mimeType))
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { fileLauncher.launch("*/*") }) {
            Icon(Icons.Default.AttachFile, contentDescription = "Adjuntar", tint = Color(0xFF555555))
        }

        BasicTextField(
            value = texto,
            onValueChange = onTextoChange,
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF111111)),
            decorationBox = { inner ->
                if (texto.isEmpty()) {
                    Text("Escribe un mensaje...", fontSize = 14.sp, color = Color(0xFFAAAAAA))
                }
                inner()
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (texto.isNotBlank()) Color(0xFF111111) else Color(0xFFDDDDDD),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onEnviar, enabled = texto.isNotBlank()) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}