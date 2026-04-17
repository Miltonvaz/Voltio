package com.miltonvaz.voltio1.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TypingIndicator(nombre: String, modifier: Modifier = Modifier) {
    Text(
        text = "✦ $nombre está escribiendo...",
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic,
        color = Color.White,
        modifier = modifier
            .background(Color(0xFF111111), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    )
}