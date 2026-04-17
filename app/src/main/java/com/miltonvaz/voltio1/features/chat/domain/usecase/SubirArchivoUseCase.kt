package com.miltonvaz.voltio1.features.chat.domain.usecase

import com.miltonvaz.voltio1.features.chat.domain.entities.Mensaje
import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import javax.inject.Inject

class SubirArchivoUseCase @Inject constructor(private val repo: IChatRepository) {
    suspend operator fun invoke(
        idConversacion: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        idRemitente: Int,
        caption: String? = null,
        idMensajeReply: Int? = null
    ): Result<Mensaje> = runCatching {
        repo.subirArchivo(idConversacion, fileBytes, fileName, mimeType, idRemitente, caption, idMensajeReply)
    }
}