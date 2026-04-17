package com.miltonvaz.voltio1.features.chat.domain.usecase

import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import javax.inject.Inject

class MarcarLeidoUseCase @Inject constructor(private val repo: IChatRepository) {
    operator fun invoke(idConversacion: Int, idUsuario: Int) =
        repo.markRead(idConversacion, idUsuario)
}