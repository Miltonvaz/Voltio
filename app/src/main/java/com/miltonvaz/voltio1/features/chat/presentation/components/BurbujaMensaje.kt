package com.miltonvaz.voltio1.features.chat.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.miltonvaz.voltio1.features.chat.domain.entities.Mensaje
import kotlin.math.roundToInt

@Composable
fun BurbujaMensaje(
    mensaje: Mensaje,
    esMio: Boolean,
    onReply: (Mensaje) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var offsetX by remember { mutableFloatStateOf(0f) }
    var replyTriggered by remember { mutableStateOf(false) }

    val animatedOffset by animateFloatAsState(targetValue = offsetX, label = "swipe")

    val bgColor = if (esMio) Color(0xFF111111) else Color(0xFFF0F0F0)
    val textColor = if (esMio) Color.White else Color(0xFF222222)
    val timeColor = if (esMio) Color.White.copy(alpha = 0.6f) else Color(0xFF888888)

    val bottomStartRadius = if (esMio) 16.dp else 4.dp
    val bottomEndRadius = if (esMio) 4.dp else 16.dp
    val shape = RoundedCornerShape(
        topStart = 16.dp, topEnd = 16.dp,
        bottomStart = bottomStartRadius, bottomEnd = bottomEndRadius
    )

    val hora = remember(mensaje.created_at) {
        runCatching {
            mensaje.created_at.substring(11, 16)
        }.getOrDefault("")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = if (esMio) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono reply aparece al deslizar (lado izquierdo para mensajes del otro, derecho para los míos)
        if (!esMio) {
            if (animatedOffset > 20f) {
                Icon(
                    Icons.Default.Reply,
                    contentDescription = "Responder",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp).padding(end = 4.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > 80f && !replyTriggered) {
                                replyTriggered = true
                                onReply(mensaje)
                            }
                            offsetX = 0f
                            replyTriggered = false
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            // Solo permitir deslizar hacia la derecha
                            val newOffset = (offsetX + dragAmount).coerceIn(0f, 100f)
                            offsetX = newOffset
                        }
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .clip(shape)
                    .background(bgColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = if (esMio) Alignment.End else Alignment.Start
            ) {
                // Reply preview
                if (mensaje.id_mensaje_reply != null) {
                    BurbujaReply(
                        nombreRemitente = mensaje.reply_nombre_remitente,
                        contenido = mensaje.reply_contenido,
                        tipoMensaje = mensaje.reply_tipo_mensaje,
                        esMio = esMio,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // Contenido según tipo
                when (mensaje.tipo_mensaje) {
                    "imagen" -> {
                        AsyncImage(
                            model = mensaje.archivo_url,
                            contentDescription = "Imagen",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        if (!mensaje.contenido.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(mensaje.contenido, color = textColor, fontSize = 14.sp)
                        }
                    }
                    "video" -> {
                        // Placeholder de video — integrar ExoPlayer si se requiere reproducción inline
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("▶ Video", color = Color.White, fontSize = 14.sp)
                        }
                        if (!mensaje.contenido.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(mensaje.contenido, color = textColor, fontSize = 14.sp)
                        }
                    }
                    "audio" -> {
                        Text("🎵 Audio", color = textColor, fontSize = 14.sp)
                    }
                    "documento" -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (esMio) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f))
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.AttachFile, null, tint = textColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            TextButton(
                                onClick = {
                                    mensaje.archivo_url?.let { url ->
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                    }
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Descargar archivo", color = textColor, fontSize = 13.sp)
                            }
                        }
                    }
                    else -> {
                        Text(
                            text = mensaje.contenido ?: "",
                            color = textColor,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = hora,
                    color = timeColor,
                    fontSize = 10.sp,
                    textAlign = if (esMio) TextAlign.End else TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (esMio) {
            if (animatedOffset < -20f) {
                Icon(
                    Icons.Default.Reply,
                    contentDescription = "Responder",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp).padding(start = 4.dp)
                )
            }
        }
    }
}