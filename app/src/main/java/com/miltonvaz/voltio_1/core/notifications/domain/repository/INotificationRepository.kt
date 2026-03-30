package com.miltonvaz.voltio_1.core.notifications.domain.repository

interface INotificationRepository {
    suspend fun subscribeToTopic(topic: String): Result<Unit>
    suspend fun unsubscribeFromTopic(topic: String): Result<Unit>
}
