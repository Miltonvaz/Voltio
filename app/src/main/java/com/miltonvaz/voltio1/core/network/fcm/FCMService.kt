package com.miltonvaz.voltio1.core.network.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.miltonvaz.voltio1.MainActivity
import com.miltonvaz.voltio1.R
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.features.auth.domain.usecase.RegisterFCMTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var registerFCMTokenUseCase: RegisterFCMTokenUseCase

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        tokenManager.saveFCMToken(token)
        
        val authToken = tokenManager.getToken()
        val userId = tokenManager.getUserId()
        if (authToken != null && userId != -1) {
            scope.launch {
                registerFCMTokenUseCase(authToken, userId, token)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        // El servidor debe enviar "type" en el data payload: "admin" o "user"
        val type = message.data["type"] ?: "user"
        val title = message.data["title"] ?: message.notification?.title ?: "Voltio"
        val body = message.data["body"] ?: message.notification?.body ?: "Tienes una actualización"
        
        showNotification(title, body, type)
    }

    private fun showNotification(title: String, body: String, type: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Separamos por canales para que el sistema los trate diferente
        val channelId = if (type == "admin") "admin_channel" else "user_channel"
        val channelName = if (type == "admin") "Pedidos Nuevos (Admin)" else "Estado de mis Pedidos"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Notificaciones de Voltio"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
