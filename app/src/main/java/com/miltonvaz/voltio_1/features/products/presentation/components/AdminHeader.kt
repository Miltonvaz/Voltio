package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.R
@Composable
fun AdminHeader(title: String, subtitle: String) {

    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.voltio),
                contentDescription = null,
                modifier = Modifier.size(42.dp)
            )
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.3f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = Color(0xFF1E293B)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E293B),
            letterSpacing = (-0.5).sp
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E293B).copy(alpha = 0.6f)
        )
    }
}