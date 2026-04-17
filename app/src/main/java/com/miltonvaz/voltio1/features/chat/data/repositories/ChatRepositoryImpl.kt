package com.miltonvaz.voltio1.features.chat.data.repositories

import com.miltonvaz.voltio1.core.network.VoltioSocketManager
import com.miltonvaz.voltio1.features.chat.data.datasource.remote.api.ChatApiService
import com.miltonvaz.voltio1.features.chat.data.datasource.remote.mapper.toDomain
import com.miltonvaz.voltio1.features.chat.domain.entities.Conversacion
import com.miltonvaz.voltio1.features.chat.domain.entities.Mensaje
import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApiService,
    private val socketManager: VoltioSocketManager
) : IChatRepository {

    override suspend fun getConversaciones(idUsuario: Int): List<Conversacion> =
        api.getConversaciones(idUsuario).map { it.toDomain() }

    override suspend fun getMensajes(idConversacion: Int): List<Mensaje> =
        api.getMensajes(idConversacion).map { it.toDomain() }

    override suspend fun subirArchivo(
        idConversacion: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        idRemitente: Int,
        caption: String?,
        idMensajeReply: Int?
    ): Mensaje {
        val filePart = MultipartBody.Part.createFormData(
            "file", fileName,
            fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())
        )
        val remitenteBody = idRemitente.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val captionBody = caption?.toRequestBody("text/plain".toMediaTypeOrNull())
        val replyBody = idMensajeReply?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        return api.subirArchivo(idConversacion, filePart, remitenteBody, captionBody, replyBody).toDomain()
    }

    override fun enviarMensaje(idConversacion: Int, idRemitente: Int, contenido: String, idMensajeReply: Int?) {
        val data = JSONObject().apply {
            put("id_conversacion", idConversacion)
            put("id_remitente", idRemitente)
            put("contenido", contenido)
            put("id_mensaje_reply", idMensajeReply ?: JSONObject.NULL)
        }
        socketManager.emit("send_message", data)
    }

    override fun joinConversacion(idConversacion: Int) =
        socketManager.emit("join_conversation", idConversacion)

    override fun leaveConversacion(idConversacion: Int) =
        socketManager.emit("leave_conversation", idConversacion)

    override fun joinEmpresa(idEmpresa: Int) =
        socketManager.emit("join_empresa", idEmpresa)

    override fun emitTyping(idConversacion: Int, idUsuario: Int, nombre: String) {
        val data = JSONObject().apply {
            put("id_conversacion", idConversacion)
            put("id_usuario", idUsuario)
            put("nombre", nombre)
        }
        socketManager.emit("typing", data)
    }

    override fun emitStopTyping(idConversacion: Int, idUsuario: Int) {
        val data = JSONObject().apply {
            put("id_conversacion", idConversacion)
            put("id_usuario", idUsuario)
        }
        socketManager.emit("stop_typing", data)
    }

    override fun markRead(idConversacion: Int, idUsuario: Int) {
        val data = JSONObject().apply {
            put("id_conversacion", idConversacion)
            put("id_usuario", idUsuario)
        }
        socketManager.emit("mark_read", data)
    }

    override fun observeNuevoMensaje(): Flow<Mensaje> =
        socketManager.observeEvent("new_message").map { raw ->
            val json = JSONObject(raw)
            Mensaje(
                id_mensaje = json.getInt("id_mensaje"),
                id_conversacion = json.getInt("id_conversacion"),
                id_remitente = json.getInt("id_remitente"),
                nombre_remitente = json.optString("nombre_remitente").ifEmpty { null },
                contenido = json.optString("contenido").ifEmpty { null },
                tipo_mensaje = json.optString("tipo_mensaje", "texto"),
                archivo_url = json.optString("archivo_url").ifEmpty { null },
                leido = json.optBoolean("leido", false),
                created_at = json.optString("created_at", ""),
                id_mensaje_reply = if (json.isNull("id_mensaje_reply")) null else json.optInt("id_mensaje_reply"),
                reply_contenido = json.optString("reply_contenido").ifEmpty { null },
                reply_nombre_remitente = json.optString("reply_nombre_remitente").ifEmpty { null },
                reply_tipo_mensaje = json.optString("reply_tipo_mensaje").ifEmpty { null },
                reply_archivo_url = json.optString("reply_archivo_url").ifEmpty { null }
            )
        }

    override fun observeTyping(): Flow<String> =
        socketManager.observeEvent("user_typing").map { raw ->
            JSONObject(raw).optString("nombre", "Alguien")
        }

    override fun observeStopTyping(): Flow<Unit> =
        socketManager.observeEvent("user_stop_typing").map { }

    override fun observeMessagesRead(): Flow<Int> =
        socketManager.observeEvent("messages_read").map { raw ->
            JSONObject(raw).getInt("id_conversacion")
        }

    override fun observeBadgeUpdate(): Flow<Pair<Int, String?>> =
        socketManager.observeEvent("badge_update").map { raw ->
            val json = JSONObject(raw)
            Pair(json.getInt("id_conversacion"), json.optString("ultimo_mensaje").ifEmpty { null })
        }

    override fun observeChatError(): Flow<String> =
        socketManager.observeEvent("chat_error").map { raw ->
            JSONObject(raw).optString("message", "Error en el chat")
        }
}