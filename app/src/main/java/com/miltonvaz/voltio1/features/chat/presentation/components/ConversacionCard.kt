package com.miltonvaz.voltio1.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.miltonvaz.voltio1.features.chat.domain.entities.Conversacion

@Composable
fun ConversacionCard(
    conversacion: Conversacion,
    onClick: () -> Unit
) {
    val inicial = conversacion.nombre_usuario.firstOrNull()?.uppercase() ?: "?"
    val hora = conversacion.ultimo_mensaje_fecha?.let { fecha ->
        runCatching { fecha.substring(11, 16) }.getOrDefault(fecha)
    } ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar con badge
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF111111)),
                contentAlignment = Alignment.Center
            ) {
                Text(inicial, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            if (conversacion.no_leidos > 0) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE53935)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversacion.no_leidos.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = conversacion.nombre_usuario,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = conversacion.ultimo_mensaje ?: "Sin mensajes",
                fontSize = 13.sp,
                color = Color(0xFF777777),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = hora,
            fontSize = 11.sp,
            color = Color(0xFF999999)
        )
    }
}