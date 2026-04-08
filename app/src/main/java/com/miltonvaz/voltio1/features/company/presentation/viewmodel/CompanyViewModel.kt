package com.miltonvaz.voltio1.features.company.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miltonvaz.voltio1.features.company.domain.usecase.GetCompanyByIdUseCase
import com.miltonvaz.voltio1.features.company.domain.usecase.GetCompanyProductsUseCase
import com.miltonvaz.voltio1.features.company.presentation.screens.UiState.CompanyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCompanyByIdUseCase: GetCompanyByIdUseCase,
    private val getCompanyProductsUseCase: GetCompanyProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyUiState())
    val uiState = _uiState.asStateFlow()

    fun loadCompany(companyId: Int) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val companyResult = getCompanyByIdUseCase(companyId)
            companyResult.fold(
                onSuccess = { company ->
                    _uiState.update { it.copy(company = company) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                    return@launch
                }
            )

            val productsResult = getCompanyProductsUseCase(companyId)
            productsResult.fold(
                onSuccess = { products ->
                    _uiState.update { it.copy(isLoading = false, products = products) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }
}
