package com.miltonvaz.voltio1.features.chat.data.datasource.remote.mapper

import com.miltonvaz.voltio1.features.chat.data.datasource.remote.model.ConversacionDto
import com.miltonvaz.voltio1.features.chat.data.datasource.remote.model.MensajeDto
import com.miltonvaz.voltio1.features.chat.domain.entities.Conversacion
import com.miltonvaz.voltio1.features.chat.domain.entities.Mensaje

fun ConversacionDto.toDomain() = Conversacion(
    id_conversacion = id_conversacion,
    id_usuario = id_usuario,
    id_empresa = id_empresa,
    nombre_usuario = nombre_usuario,
    ultimo_mensaje = ultimo_mensaje,
    ultimo_mensaje_fecha = ultimo_mensaje_fecha,
    no_leidos = no_leidos
)

fun MensajeDto.toDomain() = Mensaje(
    id_mensaje = id_mensaje,
    id_conversacion = id_conversacion,
    id_remitente = id_remitente,
    nombre_remitente = nombre_remitente,
    contenido = contenido,
    tipo_mensaje = tipo_mensaje,
    archivo_url = archivo_url,
    leido = leido,
    created_at = created_at,
    id_mensaje_reply = id_mensaje_reply,
    reply_contenido = reply_contenido,
    reply_nombre_remitente = reply_nombre_remitente,
    reply_tipo_mensaje = reply_tipo_mensaje,
    reply_archivo_url = reply_archivo_url
)