package com.miltonvaz.voltio1

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.example.compose.AppTheme
import com.google.firebase.messaging.FirebaseMessaging
import com.miltonvaz.voltio1.core.navigation.NavigationWrapper
import com.miltonvaz.voltio1.features.auth.di.navigation.AuthNavGraph
import com.miltonvaz.voltio1.features.chat.di.navigation.ChatNavGraph
import com.miltonvaz.voltio1.features.delivery.di.navigation.DeliveryNavGraph
import com.miltonvaz.voltio1.features.orders.di.navigation.OrdersNavGraph
import com.miltonvaz.voltio1.features.products.di.navigation.ProductNavGraph
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.core.service.VoltioSocketService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class   MainActivity : FragmentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var productNavGraph: ProductNavGraph

    @Inject
    lateinit var authNavGraph: AuthNavGraph

    @Inject
    lateinit var ordersNavGraph: OrdersNavGraph

    @Inject
    lateinit var deliveryNavGraph: DeliveryNavGraph

    @Inject
    lateinit var chatNavGraph: ChatNavGraph

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM", "Permiso de notificaciones concedido")
        } else {
            Log.w("FCM", "Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        askNotificationPermission()

        // Si ya hay sesión activa de empresa/admin, arrancar el servicio
        val role = tokenManager.getUserRole()
        if (role == "company" || role == "admin") {
            VoltioSocketService.start(this)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fallo al obtener el token", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM_TOKEN", "Tu Token es: $token")
        }

        setContent {
            AppTheme {
                NavigationWrapper(
                    navGraphs = listOf(authNavGraph, productNavGraph, ordersNavGraph, deliveryNavGraph, chatNavGraph)
                )
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
