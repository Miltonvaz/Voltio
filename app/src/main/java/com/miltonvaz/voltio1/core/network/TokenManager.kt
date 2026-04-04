package com.miltonvaz.voltio1.core.network

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

    fun getToken(): String? = prefs.getString("auth_token", null)

    fun saveUserId(userId: Int) {
        prefs.edit().putInt("user_id", userId).apply()
    }

    fun getUserId(): Int = prefs.getInt("user_id", -1)

    fun saveUserRole(role: String) {
        prefs.edit().putString("user_role", role).apply()
    }

    fun getUserRole(): String? = prefs.getString("user_role", null)

    fun saveFCMToken(token: String) {
        prefs.edit().putString("fcm_token", token).apply()
    }

    fun getFCMToken(): String? = prefs.getString("fcm_token", null)

    fun saveCompanyId(companyId: Int) {
        prefs.edit().putInt("company_id", companyId).apply()
    }

    fun getCompanyId(): Int = prefs.getInt("company_id", -1)

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
        prefs.edit().clear().apply()
    }
}
