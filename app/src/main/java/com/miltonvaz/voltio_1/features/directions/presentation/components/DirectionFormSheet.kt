package com.miltonvaz.voltio_1.features.directions.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miltonvaz.voltio_1.core.ui.components.CustomTextField
import com.miltonvaz.voltio_1.core.ui.components.PrimaryButton
import com.miltonvaz.voltio_1.core.ui.theme.displayFontFamily
import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionRequest
import com.miltonvaz.voltio_1.features.directions.domain.entities.Direction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectionFormSheet(
    directionToEdit: Direction? = null,
    onDismiss: () -> Unit,
    onSave: (DirectionRequest) -> Unit
) {
    var alias by remember { mutableStateOf(directionToEdit?.alias ?: "") }
    var direccion by remember { mutableStateOf(directionToEdit?.direccion ?: "") }
    var esPredeterminada by remember { mutableStateOf(directionToEdit?.es_predeterminada ?: false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            Text(
                text = if (directionToEdit != null) "Editar dirección" else "Nueva dirección",
                fontFamily = displayFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C2E)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (directionToEdit != null) "Modifica los datos de tu dirección" else "Agrega una nueva dirección de envío",
                fontFamily = displayFontFamily,
                fontSize = 13.sp,
                color = Color(0xFF616161)
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                value = alias,
                onValueChange = { alias = it },
                label = "Alias (ej. Casa, Trabajo)"
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = "Dirección completa"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = esPredeterminada,
                    onCheckedChange = { esPredeterminada = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4F46E5))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Establecer como dirección principal",
                    fontFamily = displayFontFamily,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1C2E)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = if (directionToEdit != null) "GUARDAR CAMBIOS" else "AGREGAR DIRECCIÓN",
                onClick = {
                    onSave(
                        DirectionRequest(
                            id_usuario = 0,
                            alias = alias.ifBlank { null },
                            direccion = direccion,
                            es_predeterminada = esPredeterminada
                        )
                    )
                },
                enabled = direccion.isNotBlank()
            )
        }
    }
}