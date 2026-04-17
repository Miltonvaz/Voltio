package com.miltonvaz.voltio1.features.chat.domain.usecase

import com.miltonvaz.voltio1.features.chat.domain.repositories.IChatRepository
import javax.inject.Inject

class ObserveTypingUseCase @Inject constructor(private val repo: IChatRepository) {
    operator fun invoke() = repo.observeTyping()
    fun stopTyping() = repo.observeStopTyping()
}