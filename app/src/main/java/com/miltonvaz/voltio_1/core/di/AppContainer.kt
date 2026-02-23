package com.miltonvaz.voltio_1.core.di

import android.content.Context
import com.miltonvaz.voltio_1.core.network.TokenManager
import com.miltonvaz.voltio_1.core.network.VoltioApi
import com.miltonvaz.voltio_1.features.auth.data.repositories.AuthRepositoryImpl
import com.miltonvaz.voltio_1.features.auth.domain.repositories.IAuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val context: Context) {

    val sessionManager: TokenManager by lazy {
        TokenManager(context)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://voltio.ameth.shop/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val voltioApi: VoltioApi by lazy {
        retrofit.create(VoltioApi::class.java)
    }

    val authRepository: IAuthRepository by lazy {
        AuthRepositoryImpl(voltioApi)
    }
}