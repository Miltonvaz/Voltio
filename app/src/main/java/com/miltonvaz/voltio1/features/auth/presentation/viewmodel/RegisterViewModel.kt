package com.miltonvaz.voltio1.features.auth.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.miltonvaz.voltio1.core.hardware.domain.CameraManager
import com.miltonvaz.voltio1.core.network.TokenManager
import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.*
import com.miltonvaz.voltio1.features.auth.domain.usecase.AuthUseCase
import com.miltonvaz.voltio1.features.auth.domain.usecase.GoogleAuthUseCase
import com.miltonvaz.voltio1.features.auth.domain.usecase.RegisterFCMTokenUseCase
import com.miltonvaz.voltio1.features.auth.presentation.screens.register.RegisterUiState
import com.miltonvaz.voltio1.features.delivery.domain.usecase.RegisterRepartidorInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val googleAuthUseCase: GoogleAuthUseCase,
    private val registerFCMTokenUseCase: RegisterFCMTokenUseCase,
    private val registerRepartidorInfoUseCase: RegisterRepartidorInfoUseCase,
    private val sessionManager: TokenManager,
    private val cameraManager: CameraManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    // Logo state for company registration
    var selectedLogoBytes by mutableStateOf<ByteArray?>(null)
        private set
    var logoName by mutableStateOf("Subir logo de la empresa")
        private set
    private var pendingLogoUri: Uri? = null

    fun createLogoUri(): Uri {
        val uri = cameraManager.createTempImageUri()
        pendingLogoUri = uri
        return uri
    }

    fun onLogoCameraResult(success: Boolean) {
        if (success) {
            pendingLogoUri?.let { uri ->
                selectedLogoBytes = cameraManager.readImageBytes(uri)
                logoName = "¡Foto tomada!"
            }
        }
    }

    fun onLogoGalleryResult(uri: Uri) {
        selectedLogoBytes = cameraManager.readImageBytes(uri)
        logoName = "¡Logo cargado!"
    }

    fun onGoogleSignIn(context: Context, role: String) {
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
                    
                    if (role == "user") {
                        val authResult = googleAuthUseCase(googleIdToken)
                        authResult.fold(
                            onSuccess = { response -> handleSuccess(response, true) },
                            onFailure = { error -> _uiState.update { it.copy(isLoading = false, error = error.message) } }
                        )
                    } else {
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                googleIdToken = googleIdToken, 
                                isGoogleLinked = true 
                            ) 
                        }
                    }
                }
            } catch (e: GetCredentialException) {
                _uiState.update { it.copy(isLoading = false) }
                handleGoogleError(e)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Fallo autenticación con Google") }
            }
        }
    }

    private fun handleGoogleError(e: GetCredentialException) {
        val errorMsg = if (e is NoCredentialException) "No hay cuentas de Google" else "Error: ${e.message}"
        _uiState.update { it.copy(error = errorMsg) }
    }

    fun registerUser(name: String, lastName: String, email: String, pass: String, phone: String) {
        executeRegister(email, pass) { 
            authUseCase.register(
                AuthRequest(
                    name = name.trim(), 
                    lastname = lastName.trim(), 
                    email = email.trim(), 
                    password = pass, 
                    phone = phone.trim(), 
                    role = "user", 
                    account_type = "person"
                )
            ) 
        }
    }

    fun registerCompany(
        name: String, 
        lastName: String, 
        email: String, 
        pass: String, 
        phone: String, 
        commercialName: String, 
        address: String,
        latitude: Double,
        longitude: Double,
        googleIdToken: String? = null
    ) {
        executeRegister(email, pass) {
            if (googleIdToken != null) {
                googleAuthUseCase.registerProgresive(
                    GoogleAuthRequest(
                        idToken = googleIdToken,
                        role = "company",
                        commercialName = commercialName,
                        address = address,
                        latitude = latitude,
                        longitude = longitude
                    )
                )
            } else {
                authUseCase.registerCompany(
                    CompanyRegisterRequest(
                        name = name.trim(), 
                        lastname = lastName.trim(), 
                        email = email.trim(), 
                        password = pass, 
                        phone = phone.trim(), 
                        commercialName = commercialName, 
                        address = address, 
                        latitude = latitude, 
                        longitude = longitude, 
                        contactPhone = phone.trim(), 
                        contactEmail = email.trim()
                    ),
                    logoBytes = selectedLogoBytes
                )
            }
        }
    }

    fun registerDelivery(
        name: String, 
        lastName: String, 
        email: String, 
        pass: String, 
        phone: String, 
        vehicle: String, 
        plates: String?,
        googleIdToken: String? = null
    ) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            if (googleIdToken != null) {
                // CASO GOOGLE: Usamos el endpoint progresivo enviando todo junto
                googleAuthUseCase.registerProgresive(
                    GoogleAuthRequest(
                        idToken = googleIdToken,
                        role = "repartidor",
                        vehicle = vehicle,
                        plates = plates
                    )
                ).fold(
                    onSuccess = { response ->
                        handleSuccess(response, true)
                    },
                    onFailure = { error ->
                        Log.e("REGISTER", "Error Google Progresive: ${error.message}")
                        _uiState.update { it.copy(isLoading = false, error = error.message) }
                    }
                )
            } else {
                // CASO MANUAL: Flujo de 2 pasos
                authUseCase.register(
                    AuthRequest(
                        name = name.trim(),
                        lastname = lastName.trim(),
                        email = email.trim(),
                        password = pass,
                        phone = phone.trim(),
                        role = "repartidor",
                        account_type = "person"
                    )
                ).fold(
                    onSuccess = { response ->
                        var finalToken = response.accessToken
                        var userId = response.user.id

                        if (finalToken == null) {
                            val loginResult = authUseCase(LoginRequest(email.trim(), pass))
                            loginResult.fold(
                                onSuccess = { loginResponse ->
                                    finalToken = loginResponse.accessToken
                                    userId = loginResponse.user.id
                                    handleSuccess(loginResponse, false)
                                    completeRepartidorRegistration(finalToken, userId, vehicle, plates)
                                },
                                onFailure = { error ->
                                    _uiState.update { it.copy(isLoading = false, error = "Error al obtener sesión tras el registro: ${error.message}") }
                                }
                            )
                        } else {
                            handleSuccess(response, false)
                            completeRepartidorRegistration(finalToken, userId, vehicle, plates)
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isLoading = false, error = "Error en Paso 1: ${error.message}") }
                    }
                )
            }
        }
    }

    private suspend fun completeRepartidorRegistration(token: String?, userId: Int, vehicle: String, plates: String?) {
        if (token != null) {
            registerRepartidorInfoUseCase(
                token = "Bearer $token",
                userId = userId,
                vehicle = vehicle,
                plates = plates
            ).fold(
                onSuccess = { 
                    Log.d("REGISTER", "Repartidor registrado con éxito en el servidor")
                    _uiState.update { currentState ->
                        currentState.copy(
                            isSuccess = true,
                            isLoading = false,
                            user = currentState.user?.copy(role = "repartidor")
                        )
                    }
                },
                onFailure = { error ->
                    Log.e("REGISTER", "Error Paso 2 (Crear info repartidor): ${error.message}")
                    _uiState.update { it.copy(isLoading = false, error = "Error en datos de vehículo: ${error.message}") }
                }
            )
        } else {
            _uiState.update { it.copy(isLoading = false, error = "Falta el token de sesión para completar el registro") }
        }
    }

    private fun executeRegister(email: String? = null, pass: String? = null, call: suspend () -> Result<AuthResponse>) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            call().fold(
                onSuccess = { response -> 
                    if (response.accessToken == null && email != null && pass != null) {
                        viewModelScope.launch {
                            authUseCase(LoginRequest(email.trim(), pass)).fold(
                                onSuccess = { handleSuccess(it, true) },
                                onFailure = { handleSuccess(response, true) }
                            )
                        }
                    } else {
                        handleSuccess(response, true)
                    }
                },
                onFailure = { error -> _uiState.update { it.copy(isLoading = false, error = error.message) } }
            )
        }
    }

    private suspend fun handleSuccess(response: AuthResponse, shouldNavigate: Boolean) {
        val roleToSave = if (!shouldNavigate) "repartidor" else response.user.role
        sessionManager.saveUserRole(roleToSave)
        response.accessToken?.let { sessionManager.saveToken(it) }
        sessionManager.saveUserId(response.user.id)
        
        val fcmToken = sessionManager.getFCMToken()
        val currentToken = response.accessToken
        if (fcmToken != null && currentToken != null) {
            registerFCMTokenUseCase(currentToken, response.user.id, fcmToken)
        }

        if (shouldNavigate) {
            _uiState.update { it.copy(isLoading = false, user = response.user, isSuccess = true) }
        } else {
            _uiState.update { it.copy(user = response.user) }
        }
    }

    fun clearError() { _uiState.update { it.copy(error = null) } }
}
