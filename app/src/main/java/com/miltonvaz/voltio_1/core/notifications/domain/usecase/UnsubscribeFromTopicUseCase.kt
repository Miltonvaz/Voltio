package com.miltonvaz.voltio_1.core.notifications.domain.usecase

import com.miltonvaz.voltio_1.core.notifications.domain.repository.INotificationRepository
import javax.inject.Inject

class UnsubscribeFromTopicUseCase @Inject constructor(
    private val repository: INotificationRepository
) {
    suspend operator fun invoke(topic: String): Result<Unit> {
        return repository.unsubscribeFromTopic(topic)
    }
}
