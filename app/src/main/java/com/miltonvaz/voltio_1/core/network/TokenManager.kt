package com.miltonvaz.voltio_1.core.network

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("voltio_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun clearSession() {
        prefs.edit().remove("auth_token").apply()
    }
}