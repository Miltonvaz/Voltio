package com.miltonvaz.voltio1.features.company.presentation.screens.UiState

import com.miltonvaz.voltio1.features.auth.domain.entities.Company
import com.miltonvaz.voltio1.features.products.domain.entities.Product

data class CompanyUiState(
    val isLoading: Boolean = false,
    val company: Company? = null,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
