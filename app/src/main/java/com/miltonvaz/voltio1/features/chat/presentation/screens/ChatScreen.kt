package com.miltonvaz.voltio1.features.chat.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.miltonvaz.voltio1.features.chat.presentation.components.*
import com.miltonvaz.voltio1.features.chat.presentation.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    idConversacion: Int,
    nombreUsuario: String,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Init al montar
    LaunchedEffect(idConversacion) {
        viewModel.init(idConversacion)
    }

    // Scroll al final cuando llegan nuevos mensajes
    LaunchedEffect(uiState.mensajes.size) {
        if (uiState.mensajes.isNotEmpty()) {
            scope.launch { listState.animateScrollToItem(uiState.mensajes.size - 1) }
        }
    }

    // Leave al salir
    DisposableEffect(Unit) {
        onDispose { viewModel.onLeave() }
    }

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = nombreUsuario.firstOrNull()?.uppercase() ?: "?",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(nombreUsuario, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Cliente", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF111111))
            )
        },
        bottomBar = {
            Column {
                // Reply bar
                AnimatedVisibility(visible = uiState.replyMensaje != null) {
                    uiState.replyMensaje?.let { reply ->
                        ReplyBar(
                            mensaje = reply,
                            onCancel = { viewModel.setReplyMensaje(null) }
                        )
                    }
                }

                // Preview de archivo
                AnimatedVisibility(visible = uiState.archivoSeleccionado != null) {
                    uiState.archivoSeleccionado?.let { archivo ->
                        ArchivoPreviewBar(
                            archivo = archivo,
                            isSending = uiState.isSendingFile,
                            onCaptionChange = viewModel::onCaptionChange,
                            onCancel = { viewModel.setArchivoSeleccionado(null) },
                            onEnviar = viewModel::enviarArchivo
                        )
                    }
                }

                // Input normal (solo si no hay archivo)
                if (uiState.archivoSeleccionado == null) {
                    ChatInputBar(
                        texto = uiState.textoInput,
                        onTextoChange = viewModel::onTextoChange,
                        onEnviar = viewModel::enviarMensaje,
                        onArchivoSeleccionado = { viewModel.setArchivoSeleccionado(it) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF111111)
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.mensajes, key = { it.id_mensaje }) { mensaje ->
                        BurbujaMensaje(
                            mensaje = mensaje,
                            esMio = mensaje.id_remitente == viewModel.idUsuario,
                            onReply = { viewModel.setReplyMensaje(it) }
                        )
                    }
                }
            }

            // Typing indicator
            AnimatedVisibility(
                visible = uiState.typingNombre != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 8.dp)
            ) {
                uiState.typingNombre?.let { nombre ->
                    TypingIndicator(nombre = nombre)
                }
            }
        }
    }

    // Snackbar de error
    uiState.error?.let { error ->
        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = Color(0xFF111111),
            action = { TextButton(onClick = viewModel::clearError) { Text("OK", color = Color.White) } }
        ) { Text(error) }
    }
}

@Composable
private fun ArchivoPreviewBar(
    archivo: com.miltonvaz.voltio1.features.chat.presentation.screens.UiState.ArchivoAdjunto,
    isSending: Boolean,
    onCaptionChange: (String) -> Unit,
    onCancel: () -> Unit,
    onEnviar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Thumbnail / ícono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    archivo.esImagen -> AsyncImage(
                        model = archivo.bytes,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    archivo.esVideo -> Icon(Icons.Default.VideoLibrary, null, tint = Color(0xFF555555))
                    else -> Icon(Icons.Default.AttachFile, null, tint = Color(0xFF555555))
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = archivo.nombre,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    color = Color(0xFF111111)
                )
                OutlinedTextField(
                    value = archivo.caption,
                    onValueChange = onCaptionChange,
                    placeholder = { Text("Caption (opcional)", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )
            }

            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, null, tint = Color(0xFF555555))
            }

            if (isSending) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp), color = Color(0xFF111111), strokeWidth = 2.dp)
            } else {
                IconButton(
                    onClick = onEnviar,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF111111), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = "Enviar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}