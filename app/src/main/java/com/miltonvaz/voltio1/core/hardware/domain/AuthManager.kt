package com.miltonvaz.voltio1.core.hardware.domain

interface AuthManager {
    fun canAuthenticate(): Boolean
    fun authenticate(
        activity: androidx.fragment.app.FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}