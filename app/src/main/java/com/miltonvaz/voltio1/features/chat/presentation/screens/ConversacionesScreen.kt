package com.miltonvaz.voltio1.features.chat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miltonvaz.voltio1.features.chat.presentation.components.ConversacionCard
import com.miltonvaz.voltio1.features.chat.presentation.viewmodel.ConversacionesViewModel

@Composable
fun ConversacionesScreen(
    onConversacionClick: (idConversacion: Int, nombreUsuario: String) -> Unit,
    viewModel: ConversacionesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "CHAT",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )
            Text(
                text = "Panel de vendedor",
                fontSize = 13.sp,
                color = Color(0xFF888888)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Buscador
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = uiState.query,
                    onValueChange = viewModel::onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF111111)),
                    decorationBox = { inner ->
                        if (uiState.query.isEmpty()) {
                            Text("Buscar conversación...", fontSize = 14.sp, color = Color(0xFFAAAAAA))
                        }
                        inner()
                    }
                )
            }
        }

        HorizontalDivider(color = Color(0xFFF0F0F0))

        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF111111))
                }
            }
            uiState.filtradas.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFFCCCCCC)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Sin conversaciones", fontSize = 15.sp, color = Color(0xFFAAAAAA))
                    }
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.filtradas, key = { it.id_conversacion }) { conv ->
                        ConversacionCard(
                            conversacion = conv,
                            onClick = { onConversacionClick(conv.id_conversacion, conv.nombre_usuario) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 76.dp),
                            color = Color(0xFFF0F0F0),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }

    // Error Snackbar
    uiState.error?.let { error ->
        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = Color(0xFF111111),
            action = { TextButton(onClick = viewModel::clearError) { Text("OK", color = Color.White) } }
        ) { Text(error) }
    }
}