package com.miltonvaz.voltio1.features.chat.domain.usecase

import com.miltonvaz.voltio1.features.chat.domain.entities.Mensaje
import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import javax.inject.Inject

class GetMensajesUseCase @Inject constructor(private val repo: IChatRepository) {
    suspend operator fun invoke(idConversacion: Int): Result<List<Mensaje>> = runCatching {
        repo.getMensajes(idConversacion)
    }
}