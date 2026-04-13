package com.miltonvaz.voltio1.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.miltonvaz.voltio1.MainActivity
import com.miltonvaz.voltio1.R
import com.miltonvaz.voltio1.core.network.ISocketManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class VoltioSocketService : Service() {

    @Inject
    lateinit var socketManager: ISocketManager

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        const val CHANNEL_ID_PERSISTENT  = "voltio_socket_channel"
        const val CHANNEL_ID_ORDERS      = "voltio_orders_channel"
        const val NOTIFICATION_ID        = 1001

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP  = "ACTION_STOP"

        fun start(context: Context) {
            val intent = Intent(context, VoltioSocketService::class.java).apply {
                action = ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, VoltioSocketService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                Log.d("SOCKET_SERVICE", "Iniciando Foreground Service")
                startForeground(NOTIFICATION_ID, buildPersistentNotification())
                connectAndListen()
            }
            ACTION_STOP -> {
                Log.d("SOCKET_SERVICE", "Deteniendo Foreground Service")
                socketManager.disconnect()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun connectAndListen() {
        socketManager.connect()
        Log.d("SOCKET_SERVICE", "Socket conectado, escuchando pedidos...")

        serviceScope.launch {
            socketManager.observeOrders().collect { json ->
                Log.d("SOCKET_SERVICE", "Evento recibido: $json")
                handleNewOrder(json)
            }
        }
    }

    private fun handleNewOrder(json: String) {
        try {
            val obj = JSONObject(json)
            val orderId = obj.optInt("id_orden", 0)
                .takeIf { it != 0 }
                ?: obj.optJSONObject("datos")?.optInt("id_orden", 0)
                ?: 0
            val total = obj.optDouble("monto_total", 0.0)
                .takeIf { it != 0.0 }
                ?: obj.optJSONObject("datos")?.optDouble("monto_total", 0.0)
                ?: 0.0

            val title = "¡Nuevo pedido recibido!"
            val body = if (orderId > 0) "Pedido #$orderId — $${"%.2f".format(total)}" else "Tienes un pedido nuevo"

            showOrderNotification(title, body)
        } catch (e: Exception) {
            showOrderNotification("¡Nuevo pedido!", "Revisa tus pedidos pendientes")
        }
    }

    private fun showOrderNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID_ORDERS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun buildPersistentNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID_PERSISTENT)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Voltio activo")
            .setContentText("Escuchando pedidos en tiempo real")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val persistentChannel = NotificationChannel(
                CHANNEL_ID_PERSISTENT,
                "Voltio en segundo plano",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Mantiene la conexión activa para recibir pedidos"
            }

            val ordersChannel = NotificationChannel(
                CHANNEL_ID_ORDERS,
                "Nuevos pedidos",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de pedidos nuevos"
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(persistentChannel)
            notificationManager.createNotificationChannel(ordersChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        serviceScope.cancel()
        socketManager.disconnect()
        super.onDestroy()
    }
}
