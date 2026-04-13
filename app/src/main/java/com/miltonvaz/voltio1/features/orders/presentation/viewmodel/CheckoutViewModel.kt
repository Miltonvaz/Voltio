package com.miltonvaz.voltio1.features.orders.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.features.directions.domain.usecase.DirectionUseCase
import com.miltonvaz.voltio1.features.orders.data.repositories.PayPalRepository
import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderItem
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderStatus
import com.miltonvaz.voltio1.features.orders.domain.usecase.CreateOrderUseCase
import com.miltonvaz.voltio1.features.orders.domain.usecase.cart.ClearCartUseCase
import com.miltonvaz.voltio1.features.orders.domain.usecase.cart.GetCartItemsUseCase
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.AddressInfo
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.CardInfo
import com.miltonvaz.voltio1.features.orders.presentation.screens.UiState.CheckoutUiState
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.corepayments.PayPalSDKError
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutClient
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutListener
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutRequest
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val directionUseCase: DirectionUseCase,
    private val tokenManager: TokenManager,
    private val payPalRepository: PayPalRepository,
    application: Application
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    // ── PayPal Native Client vive en el ViewModel para sobrevivir cambios de lifecycle ──
    private val payPalNativeClient: PayPalNativeCheckoutClient

    init {
        val config = CoreConfig(
            clientId = "ASSgyAE7TUCod7N73t3PLZiJ8TVIMgTceJrWVdUsRe9wEeyuCEdOd3WAz2lVdMf0e4ObyW1fzg426nwv",
            environment = Environment.SANDBOX
        )
        payPalNativeClient = PayPalNativeCheckoutClient(
            application = application,
            coreConfig = config,
            returnUrl = "com.miltonvaz.voltio1://paypalpay"
        )
        payPalNativeClient.listener = object : PayPalNativeCheckoutListener {
            override fun onPayPalCheckoutStart() {
                Log.d("PAYPAL", "PayPal paysheet abierto")
            }

            override fun onPayPalCheckoutSuccess(result: PayPalNativeCheckoutResult) {
                Log.d("PAYPAL", "Pago aprobado, orderId: ${result.orderId}")
                onPayPalSuccess(result.orderId ?: "")
            }

            override fun onPayPalCheckoutFailure(error: PayPalSDKError) {
                Log.e("PAYPAL", "Error PayPal - code: ${error.code}, desc: ${error.errorDescription}, correlationId: ${error.correlationId}")
                onPayPalError("Error PayPal (${error.code}): ${error.errorDescription}")
            }

            override fun onPayPalCheckoutCanceled() {
                Log.d("PAYPAL", "Pago cancelado por el usuario")
                onPayPalError("Pago cancelado por el usuario")
            }
        }

        loadDefaultDirection()
    }

    private fun loadDefaultDirection() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            directionUseCase.getByUserId(userId).onSuccess { directions ->
                val defaultDir = directions.find { it.es_predeterminada } ?: directions.firstOrNull()
                defaultDir?.let { dir ->
                    _uiState.update { 
                        it.copy(addressInfo = AddressInfo(
                            street = dir.direccion,
                            city = "", 
                            state = "",
                            zipCode = "",
                            reference = dir.alias,
                            latitude = dir.latitude,
                            longitude = dir.longitude
                        ))
                    }
                }
            }
        }
    }

    fun updateCardInfo(
        number: String = _uiState.value.cardInfo.number,
        name: String = _uiState.value.cardInfo.name,
        expiry: String = _uiState.value.cardInfo.expiry,
        cvv: String = _uiState.value.cardInfo.cvv
    ) {
        _uiState.update { it.copy(cardInfo = CardInfo(number, name, expiry, cvv)) }
    }

    fun updateAddressInfo(
        street: String = _uiState.value.addressInfo.street,
        city: String = _uiState.value.addressInfo.city,
        state: String = _uiState.value.addressInfo.state,
        zipCode: String = _uiState.value.addressInfo.zipCode,
        reference: String? = _uiState.value.addressInfo.reference,
        latitude: Double? = _uiState.value.addressInfo.latitude,
        longitude: Double? = _uiState.value.addressInfo.longitude
    ) {
        _uiState.update { it.copy(addressInfo = AddressInfo(street, city, state, zipCode, reference, latitude, longitude)) }
    }

    // ── Paso 2: Pedir el "Ticket" al backend ──
    fun createPayPalOrder(totalAmount: Double) {
        _uiState.update { it.copy(isLoading = true, error = null, paypalReady = false) }

        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val result = payPalRepository.createOrder(token, totalAmount)

            result.fold(
                onSuccess = { response ->
                    Log.d("PAYPAL", "Order ID recibido del backend: ${response.id}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            paypalOrderId = response.id,
                            paypalReady = true
                        )
                    }
                    // Iniciar checkout nativo directamente desde el ViewModel
                    try {
                        val request = PayPalNativeCheckoutRequest(response.id)
                        payPalNativeClient.startCheckout(request)
                        Log.d("PAYPAL", "startCheckout llamado con orderId: ${response.id}")
                    } catch (e: Exception) {
                        Log.e("PAYPAL", "Error al iniciar checkout nativo: ${e.message}", e)
                        _uiState.update {
                            it.copy(error = "Error al abrir PayPal: ${e.message}", paypalReady = false)
                        }
                    }
                },
                onFailure = { error ->
                    Log.e("PAYPAL", "Error creando orden PayPal: ${error.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al conectar con PayPal: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    // ── Paso 5: El cliente aprobó → Paso 6: Capturar el pago ──
    fun onPayPalSuccess(orderId: String) {
        Log.d("PAYPAL", "Pago aprobado por el usuario, capturando orden: $orderId")
        _uiState.update { it.copy(isLoading = true, error = null, paypalReady = false) }

        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val captureResult = payPalRepository.captureOrder(token, orderId)

            captureResult.fold(
                onSuccess = { response ->
                    Log.d("PAYPAL", "Pago capturado exitosamente: ${response.status}")
                    // Paso 7: Crear la orden en nuestro sistema
                    placeOrder(paymentType = "paypal")
                },
                onFailure = { error ->
                    Log.e("PAYPAL", "Error al capturar pago: ${error.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al cobrar el pago: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    fun onPayPalError(errorMessage: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                error = errorMessage,
                paypalReady = false,
                paypalOrderId = null
            )
        }
    }

    fun placeOrder(paymentType: String = "tarjeta") {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            val token = tokenManager.getToken() ?: ""
            val cartItems = getCartItemsUseCase().first()
            
            if (cartItems.isEmpty()) {
                _uiState.update { it.copy(isLoading = false, error = "El carrito está vacío") }
                return@launch
            }

            val addressInfo = _uiState.value.addressInfo
            val fullAddress = if (addressInfo.city.isBlank()) {
                addressInfo.street 
            } else {
                "${addressInfo.street}, ${addressInfo.city}, ${addressInfo.state} CP: ${addressInfo.zipCode}"
            }
            
            val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
            val orderDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            
            val last4 = if (paymentType == "paypal") "PAYP" else _uiState.value.cardInfo.number.takeLast(4).ifBlank { "0000" }
            val userId = tokenManager.getUserId()
            // Obtiene la empresa del primer producto del carrito (todos pertenecen a la misma empresa)
            val companyId = cartItems.firstOrNull()?.product?.companyId

            val order = Order(
                id = 0,
                userId = userId,
                companyId = companyId,
                orderDate = orderDate,
                status = OrderStatus.PENDING,
                totalAmount = totalAmount,
                description = "Pedido desde app",
                address = fullAddress,
                latitude = addressInfo.latitude,
                longitude = addressInfo.longitude,
                paymentType = paymentType,
                last4 = last4,
                products = cartItems.map {
                    OrderItem(
                        productId = it.product.id,
                        productName = it.product.name,
                        quantity = it.quantity,
                        unitPrice = it.product.price
                    )
                }
            )

            val result = createOrderUseCase(token, order, last4)
            
            _uiState.update { currentState ->
                result.fold(
                    onSuccess = {
                        viewModelScope.launch { clearCartUseCase() }
                        currentState.copy(isLoading = false, orderPlacedSuccessfully = true)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, orderPlacedSuccessfully = false, error = error.message)
                    }
                )
            }
        }
    }

    suspend fun getTotalAmount(): Double {
        return getCartItemsUseCase().first().sumOf { it.product.price * it.quantity }
    }

    override fun onCleared() {
        super.onCleared()
        payPalNativeClient.listener = null
    }
}
