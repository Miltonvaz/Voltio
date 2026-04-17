package com.miltonvaz.voltio1.features.chat.domain.usecase

import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import javax.inject.Inject

class EnviarMensajeUseCase @Inject constructor(private val repo: IChatRepository) {
    operator fun invoke(idConversacion: Int, idRemitente: Int, contenido: String, idMensajeReply: Int? = null) {
        repo.enviarMensaje(idConversacion, idRemitente, contenido, idMensajeReply)
    }
}