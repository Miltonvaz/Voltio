package com.miltonvaz.voltio_1.features.auth.di

import com.miltonvaz.voltio_1.features.auth.domain.usecase.AuthUseCase
import com.miltonvaz.voltio_1.features.auth.presentation.viewmodel.AuthViewModelFactory

class AuthModule(
    private val appContainer: AppContainer
) {

    private fun provideAuthUseCase(): AuthUseCase {
        return AuthUseCase(appContainer.authRepository)
    }

    fun provideLoginViewModelFactory(): AuthViewModelFactory {
        return AuthViewModelFactory(
            authUseCase = provideAuthUseCase(),
            sessionManager = appContainer.sessionManager
        )
    }
}