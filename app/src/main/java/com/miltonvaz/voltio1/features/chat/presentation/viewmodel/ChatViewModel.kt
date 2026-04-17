package com.miltonvaz.voltio1.features.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.features.chat.domain.entities.Mensaje
import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import com.miltonvaz.voltio1.features.chat.domain.usecase.*
import com.miltonvaz.voltio1.features.chat.presentation.screens.UiState.ArchivoAdjunto
import com.miltonvaz.voltio1.features.chat.presentation.screens.UiState.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMensajesUseCase: GetMensajesUseCase,
    private val enviarMensajeUseCase: EnviarMensajeUseCase,
    private val subirArchivoUseCase: SubirArchivoUseCase,
    private val marcarLeidoUseCase: MarcarLeidoUseCase,
    private val observeNuevoMensajeUseCase: ObserveNuevoMensajeUseCase,
    private val observeTypingUseCase: ObserveTypingUseCase,
    private val repository: IChatRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    val idUsuario: Int get() = tokenManager.getUserId() ?: -1

    private var stopTypingJob: Job? = null
    private var currentConversacionId: Int = -1

    fun init(idConversacion: Int) {
        currentConversacionId = idConversacion
        repository.joinConversacion(idConversacion)
        cargarMensajes(idConversacion)
        marcarLeidoUseCase(idConversacion, idUsuario)
        observarNuevosMensajes(idConversacion)
        observarTyping()
        observarChatError()
    }

    fun onLeave() {
        if (currentConversacionId != -1) {
            repository.leaveConversacion(currentConversacionId)
        }
    }

    private fun cargarMensajes(idConversacion: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getMensajesUseCase(idConversacion).fold(
                onSuccess = { msgs ->
                    _uiState.update { it.copy(isLoading = false, mensajes = msgs) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    private fun observarNuevosMensajes(idConversacion: Int) {
        viewModelScope.launch {
            observeNuevoMensajeUseCase().collect { mensaje ->
                if (mensaje.id_conversacion == idConversacion) {
                    _uiState.update { it.copy(mensajes = it.mensajes + mensaje) }
                }
            }
        }
    }

    private fun observarTyping() {
        viewModelScope.launch {
            observeTypingUseCase().collect { nombre ->
                _uiState.update { it.copy(typingNombre = nombre) }
            }
        }
        viewModelScope.launch {
            observeTypingUseCase.stopTyping().collect {
                _uiState.update { it.copy(typingNombre = null) }
            }
        }
    }

    private fun observarChatError() {
        viewModelScope.launch {
            repository.observeChatError().collect { msg ->
                _uiState.update { it.copy(error = msg) }
            }
        }
    }

    fun onTextoChange(texto: String) {
        _uiState.update { it.copy(textoInput = texto) }
        if (texto.isNotBlank()) {
            val nombre = tokenManager.getUserName() ?: "Usuario"
            repository.emitTyping(currentConversacionId, idUsuario, nombre)
            stopTypingJob?.cancel()
            stopTypingJob = viewModelScope.launch {
                delay(1000)
                repository.emitStopTyping(currentConversacionId, idUsuario)
            }
        }
    }

    fun enviarMensaje() {
        val texto = _uiState.value.textoInput.trim()
        if (texto.isBlank()) return
        val replyId = _uiState.value.replyMensaje?.id_mensaje
        enviarMensajeUseCase(currentConversacionId, idUsuario, texto, replyId)
        repository.emitStopTyping(currentConversacionId, idUsuario)
        _uiState.update { it.copy(textoInput = "", replyMensaje = null) }
    }

    fun setReplyMensaje(mensaje: Mensaje?) {
        _uiState.update { it.copy(replyMensaje = mensaje) }
    }

    fun setArchivoSeleccionado(archivo: ArchivoAdjunto?) {
        _uiState.update { it.copy(archivoSeleccionado = archivo) }
    }

    fun onCaptionChange(caption: String) {
        _uiState.update { state ->
            state.copy(archivoSeleccionado = state.archivoSeleccionado?.copy(caption = caption))
        }
    }

    fun enviarArchivo() {
        val archivo = _uiState.value.archivoSeleccionado ?: return
        val replyId = _uiState.value.replyMensaje?.id_mensaje
        _uiState.update { it.copy(isSendingFile = true) }
        viewModelScope.launch {
            subirArchivoUseCase(
                idConversacion = currentConversacionId,
                fileBytes = archivo.bytes,
                fileName = archivo.nombre,
                mimeType = archivo.mimeType,
                idRemitente = idUsuario,
                caption = archivo.caption.ifBlank { null },
                idMensajeReply = replyId
            ).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(isSendingFile = false, archivoSeleccionado = null, replyMensaje = null)
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isSendingFile = false, error = e.message) }
                }
            )
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}