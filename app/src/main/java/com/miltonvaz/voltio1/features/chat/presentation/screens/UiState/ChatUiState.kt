package com.miltonvaz.voltio1.features.chat.presentation.screens.UiState

import com.miltonvaz.voltio1.features.chat.domain.entities.Mensaje

data class ChatUiState(
    val isLoading: Boolean = false,
    val mensajes: List<Mensaje> = emptyList(),
    val textoInput: String = "",
    val typingNombre: String? = null,
    val replyMensaje: Mensaje? = null,
    val archivoSeleccionado: ArchivoAdjunto? = null,
    val isSendingFile: Boolean = false,
    val error: String? = null
)

data class ArchivoAdjunto(
    val bytes: ByteArray,
    val nombre: String,
    val mimeType: String,
    val caption: String = ""
) {
    val esImagen get() = mimeType.startsWith("image/")
    val esVideo get() = mimeType.startsWith("video/")
    val esDocumento get() = !esImagen && !esVideo
}