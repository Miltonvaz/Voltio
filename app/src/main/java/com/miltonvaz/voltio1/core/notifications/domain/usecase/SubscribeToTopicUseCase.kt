package com.miltonvaz.voltio1.core.notifications.domain.usecase

import com.miltonvaz.voltio1.core.notifications.domain.repository.INotificationRepository
import javax.inject.Inject

class SubscribeToTopicUseCase @Inject constructor(
    private val repository: INotificationRepository
) {
    suspend operator fun invoke(topic: String): Result<Unit> {
        return repository.subscribeToTopic(topic)
    }
}
