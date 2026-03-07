package com.miltonvaz.voltio_1.core.hardware.domain

interface AuthManager {
    fun canAuthenticate(): Boolean
    fun authenticate(
        activity: androidx.fragment.app.FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}