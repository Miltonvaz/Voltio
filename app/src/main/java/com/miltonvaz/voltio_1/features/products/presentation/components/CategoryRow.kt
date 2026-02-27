package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryRow(onCategoryClick: (String) -> Unit) {
    val categories = listOf(
        CategoryItemData("M-Control", Icons.Default.Memory),
        CategoryItemData("Sensores", Icons.Default.SettingsInputAntenna),
        CategoryItemData("Componentes", Icons.Default.ToggleOn),
        CategoryItemData("Robótica", Icons.Default.PrecisionManufacturing)
    )

    var selectedCategory by remember { mutableStateOf("M-Control") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { item ->
            val isSelected = item.label == selectedCategory
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { 
                    selectedCategory = item.label
                    onCategoryClick(item.label)
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color(0xFFE0E7FF) else Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) Color(0xFF4F46E5) else Color.LightGray,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.label,
                    fontSize = 10.sp,
                    color = if (isSelected) Color(0xFF1E1B4B) else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

data class CategoryItemData(val label: String, val icon: ImageVector)
