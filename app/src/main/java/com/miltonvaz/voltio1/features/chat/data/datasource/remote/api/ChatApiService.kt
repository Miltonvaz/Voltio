package com.miltonvaz.voltio1.features.chat.data.datasource.remote.api

import com.miltonvaz.voltio1.features.chat.data.datasource.remote.model.ConversacionDto
import com.miltonvaz.voltio1.features.chat.data.datasource.remote.model.MensajeDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ChatApiService {

    @GET("chat/usuario/{id_usuario}")
    suspend fun getConversaciones(
        @Path("id_usuario") idUsuario: Int
    ): List<ConversacionDto>

    @GET("chat/{id_conversacion}/mensajes")
    suspend fun getMensajes(
        @Path("id_conversacion") idConversacion: Int
    ): List<MensajeDto>

    @Multipart
    @POST("chat/{id_conversacion}/archivo")
    suspend fun subirArchivo(
        @Path("id_conversacion") idConversacion: Int,
        @Part file: MultipartBody.Part,
        @Part("id_remitente") idRemitente: RequestBody,
        @Part("caption") caption: RequestBody?,
        @Part("id_mensaje_reply") idMensajeReply: RequestBody?
    ): MensajeDto

    @POST("conversaciones")
    suspend fun crearOObtenerConversacion(
        @Body body: Map<String, Any>
    ): ConversacionDto
}