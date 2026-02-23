package com.miltonvaz.voltio_1.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.core.ui.theme.displayFontFamily

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB8C5E0),
            contentColor = Color(0xFF1A1C2E),
            disabledContainerColor = Color(0xFFB8C5E0).copy(alpha = 0.5f),
            disabledContentColor = Color(0xFF1A1C2E).copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = text,
            fontFamily = displayFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
