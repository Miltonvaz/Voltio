package com.miltonvaz.voltio_1.features.auth.presentation.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio_1.core.hardware.domain.AuthManager
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.LoginRequest
import com.miltonvaz.voltio_1.features.auth.domain.usecase.AuthUseCase
import com.miltonvaz.voltio_1.features.auth.presentation.screens.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val sessionManager: TokenManager,
    private val authManager: AuthManager
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

    fun login(email: String, password: String, activity: FragmentActivity? = null) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val loginRequest = LoginRequest(email, password)
            val result = authUseCase(loginRequest)

            result.fold(
                onSuccess = { response ->
                    if (response.user.role == "admin") {
                        sessionManager.saveAdminCredentials(email, password)
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
                            error = error.message ?: "Error desconocido",
                            isAuthenticated = false
                        )
                    }
                }
            )
        }
    }

    private fun finalizeLogin(token: String, user: com.miltonvaz.voltio_1.features.auth.domain.entities.Auth) {
        viewModelScope.launch {
            sessionManager.saveToken(token)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    user = user,
                    isAuthenticated = true,
                    error = null
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
