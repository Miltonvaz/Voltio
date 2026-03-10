package com.miltonvaz.voltio_1.core.network

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("voltio_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun saveUserId(userId: Int) {
        prefs.edit().putInt("user_id", userId).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt("user_id", 1)
    }

    fun saveAdminCredentials(email: String, pass: String) {
        prefs.edit()
            .putString("admin_email", email)
            .putString("admin_pass", pass)
            .apply()
    }

    fun getAdminCredentials(): Pair<String?, String?> {
        val email = prefs.getString("admin_email", null)
        val pass = prefs.getString("admin_pass", null)
        return email to pass
    }

    fun clearSession() {
        prefs.edit()
            .remove("auth_token")
            .remove("user_id")
            .remove("admin_email")
            .remove("admin_pass")
            .apply()
    }
}
