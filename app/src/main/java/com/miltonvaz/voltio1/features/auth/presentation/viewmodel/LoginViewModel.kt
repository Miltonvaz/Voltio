package com.miltonvaz.voltio1.features.auth.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.miltonvaz.voltio1.core.hardware.domain.AuthManager
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.core.notifications.domain.usecase.SubscribeToTopicUseCase
import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.LoginRequest
import com.miltonvaz.voltio1.features.auth.domain.usecase.AuthUseCase
import com.miltonvaz.voltio1.features.auth.domain.usecase.GoogleAuthUseCase
import com.miltonvaz.voltio1.features.auth.domain.usecase.RegisterFCMTokenUseCase
import com.miltonvaz.voltio1.features.auth.presentation.screens.LoginUiState
import com.miltonvaz.voltio1.core.service.VoltioSocketService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val googleAuthUseCase: GoogleAuthUseCase,
    private val registerFCMTokenUseCase: RegisterFCMTokenUseCase,
    private val subscribeToTopicUseCase: SubscribeToTopicUseCase,
    private val sessionManager: TokenManager,
    private val authManager: AuthManager,
    @ApplicationContext private val appContext: android.content.Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun canFastLogin(): Boolean {
        val (email, pass) = sessionManager.getAdminCredentials()
        return email != null && pass != null && authManager.canAuthenticate()
    }

    fun fastAdminLogin(activity: FragmentActivity) {
        val (email, pass) = sessionManager.getAdminCredentials()
        if (email != null && pass != null) {
            authManager.authenticate(
                activity = activity,
                onSuccess = {
                    login(email, pass)
                },
                onError = { error ->
                    _uiState.update { it.copy(error = "Huella no válida: $error") }
                }
            )
        }
    }

    fun onGoogleSignIn(context: Context) {
        val credentialManager = CredentialManager.create(context)
        
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("252849714413-roeet8uefhnas8dned8a1852m7rji1qg.apps.googleusercontent.com")
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val result = credentialManager.getCredential(context = context, request = request)
                val credential = result.credential
                
                if (credential is GoogleIdTokenCredential) {
                    val googleIdToken = credential.idToken
                    val authResult = googleAuthUseCase(googleIdToken)
                    
                    authResult.fold(
                        onSuccess = { response ->
                            sessionManager.saveUserRole(response.user.role)
                            finalizeLogin(response.accessToken, response.user)
                        },
                        onFailure = { error ->
                            _uiState.update { it.copy(isLoading = false, error = "Error en servidor: ${error.message}") }
                        }
                    )
                }
            } catch (e: GetCredentialException) {
                _uiState.update { it.copy(isLoading = false) }
                handleGoogleError(e)
            } catch (e: Exception) {
                Log.e("GOOGLE_AUTH", "Error inesperado: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = "Fallo inesperado en el inicio de sesión") }
            }
        }
    }

    private fun handleGoogleError(e: GetCredentialException) {
        val errorMsg = when (e) {
            is NoCredentialException -> "No hay cuentas de Google configuradas en este dispositivo"
            else -> "Error de Google: ${e.message}"
        }
        Log.e("GOOGLE_AUTH", "Error de Credenciales: $errorMsg")
        _uiState.update { it.copy(error = errorMsg) }
    }

    fun login(email: String, password: String, activity: FragmentActivity? = null) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val result = authUseCase(loginRequest)

                result.fold(
                    onSuccess = { response ->
                        sessionManager.saveUserRole(response.user.role)

                        if (response.user.role == "admin") {
                            sessionManager.saveAdminCredentials(email, password)
                            subscribeToTopicUseCase("admin_orders")

                            if (activity != null && authManager.canAuthenticate()) {
                                authManager.authenticate(
                                    activity = activity,
                                    onSuccess = {
                                        finalizeLogin(response.accessToken, response.user)
                                    },
                                    onError = { error ->
                                        _uiState.update { it.copy(isLoading = false, error = "Autenticación fallida: $error") }
                                    }
                                )
                            } else {
                                finalizeLogin(response.accessToken, response.user)
                            }
                        } else {
                            finalizeLogin(response.accessToken, response.user)
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Error de credenciales",
                                isAuthenticated = false
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("LOGIN_ERROR", "Fallo crítico en login: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = "Error de conexión") }
            }
        }
    }

    private fun finalizeLogin(token: String?, user: com.miltonvaz.voltio1.features.auth.domain.entities.Auth) {
        viewModelScope.launch {
            try {
                token?.let { sessionManager.saveToken(it) }
                sessionManager.saveUserId(user.id)

                val fcmToken = sessionManager.getFCMToken()
                if (fcmToken != null && token != null) {
                    try {
                        registerFCMTokenUseCase(token, user.id, fcmToken)
                    } catch (e: Exception) {
                        Log.w("FCM", "No se pudo registrar el token FCM: ${e.message}")
                    }
                }

                // Arrancar Foreground Service solo para empresa/admin
                if (user.role == "company" || user.role == "admin") {
                    Log.d("SOCKET_SERVICE", "Arrancando servicio para rol=${user.role}")
                    VoltioSocketService.start(appContext)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                        isAuthenticated = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                Log.e("FINALIZE_LOGIN", "Error al finalizar sesión: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = "Error al iniciar sesión") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
