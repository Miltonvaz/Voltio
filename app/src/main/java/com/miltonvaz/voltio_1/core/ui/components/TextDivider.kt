package com.miltonvaz.voltio_1.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.core.ui.theme.displayFontFamily

@Composable
fun TextDivider(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color(0xFFBDBDBD)
        )
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp),
            fontFamily = displayFontFamily,
            fontSize = 13.sp,
            color = Color(0xFF9E9E9E)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color(0xFFBDBDBD)
        )
    }
}
