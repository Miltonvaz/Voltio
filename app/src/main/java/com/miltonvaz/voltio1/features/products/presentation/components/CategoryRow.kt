package com.miltonvaz.voltio1.features.products.presentation.components

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
            .padding(horizontal = 20.dp),
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
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = if (isSelected) Color(0xFF4F46E5) else Color.White,
                    shadowElevation = if (isSelected) 6.dp else 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) Color.White else Color(0xFFB0B8C9),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.label,
                    fontSize = 11.sp,
                    color = if (isSelected) Color(0xFF1E1B4B) else Color(0xFF94A3B8),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

data class CategoryItemData(val label: String, val icon: ImageVector)
