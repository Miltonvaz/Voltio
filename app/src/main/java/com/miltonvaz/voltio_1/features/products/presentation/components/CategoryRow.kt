package com.miltonvaz.voltio_1.features.products.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miltonvaz.voltio_1.R

@Composable
fun CategoryRow(onCategoryClick: (String) -> Unit) {
    val categories = listOf(
        Pair("Microcontroladores", R.drawable.microcontrolador),
        Pair("Sensores", R.drawable.sensor),
        Pair("Componentes", R.drawable.componentes),
        Pair("Robótica", R.drawable.brazo_robotico)
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(categories) { (name, icon) ->
            CategoryItem(
                name = name,
                iconRes = icon,
                onClick = { onCategoryClick(name) }
            )
        }
    }
}