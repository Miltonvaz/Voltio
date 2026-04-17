package com.miltonvaz.voltio1.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BurbujaReply(
    nombreRemitente: String?,
    contenido: String?,
    tipoMensaje: String?,
    esMio: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor = if (esMio) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.06f)
    val barColor = if (esMio) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.3f)
    val textColor = if (esMio) Color.White else Color(0xFF333333)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(36.dp)
                .background(barColor, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nombreRemitente ?: "Usuario",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            when (tipoMensaje) {
                "imagen" -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Image, null, modifier = Modifier.size(12.dp), tint = textColor.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Imagen", fontSize = 11.sp, color = textColor.copy(alpha = 0.7f))
                }
                "video" -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VideoLibrary, null, modifier = Modifier.size(12.dp), tint = textColor.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Video", fontSize = 11.sp, color = textColor.copy(alpha = 0.7f))
                }
                "documento" -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AttachFile, null, modifier = Modifier.size(12.dp), tint = textColor.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Documento", fontSize = 11.sp, color = textColor.copy(alpha = 0.7f))
                }
                else -> Text(
                    text = contenido ?: "",
                    fontSize = 11.sp,
                    color = textColor.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}