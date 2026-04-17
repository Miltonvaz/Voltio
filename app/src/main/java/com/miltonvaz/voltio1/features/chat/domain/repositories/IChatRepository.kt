package com.miltonvaz.voltio1.features.chat.domain.repositories

import com.miltonvaz.voltio1.features.chat.domain.entities.Conversacion
import com.miltonvaz.voltio1.features.chat.domain.entities.Mensaje
import kotlinx.coroutines.flow.Flow

interface IChatRepository {
    suspend fun getConversaciones(idUsuario: Int): List<Conversacion>
    suspend fun getMensajes(idConversacion: Int): List<Mensaje>
    suspend fun subirArchivo(
        idConversacion: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        idRemitente: Int,
        caption: String?,
        idMensajeReply: Int?
    ): Mensaje
    fun enviarMensaje(idConversacion: Int, idRemitente: Int, contenido: String, idMensajeReply: Int?)
    fun joinConversacion(idConversacion: Int)
    fun leaveConversacion(idConversacion: Int)
    fun joinEmpresa(idEmpresa: Int)
    fun emitTyping(idConversacion: Int, idUsuario: Int, nombre: String)
    fun emitStopTyping(idConversacion: Int, idUsuario: Int)
    fun markRead(idConversacion: Int, idUsuario: Int)
    fun observeNuevoMensaje(): Flow<Mensaje>
    fun observeTyping(): Flow<String>
    fun observeStopTyping(): Flow<Unit>
    fun observeMessagesRead(): Flow<Int>
    fun observeBadgeUpdate(): Flow<Pair<Int, String?>>
    fun observeChatError(): Flow<String>
}