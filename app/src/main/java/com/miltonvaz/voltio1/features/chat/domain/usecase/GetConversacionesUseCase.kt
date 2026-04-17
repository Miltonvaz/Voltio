package com.miltonvaz.voltio1.features.chat.domain.usecase

import com.miltonvaz.voltio1.features.chat.domain.entities.Conversacion
import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import javax.inject.Inject

class GetConversacionesUseCase @Inject constructor(private val repo: IChatRepository) {
    suspend operator fun invoke(idUsuario: Int): Result<List<Conversacion>> = runCatching {
        repo.getConversaciones(idUsuario)
    }
}