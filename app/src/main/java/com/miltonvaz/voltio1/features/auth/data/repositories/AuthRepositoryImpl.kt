package com.miltonvaz.voltio1.features.auth.data.repositories

import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.*
import com.miltonvaz.voltio1.core.network.VoltioApi
import com.miltonvaz.voltio1.features.auth.domain.entities.Company
import com.miltonvaz.voltio1.features.auth.domain.repositories.IAuthRepository
import com.miltonvaz.voltio1.features.products.data.datasource.remote.mapper.toDomain
import com.miltonvaz.voltio1.features.products.domain.entities.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: VoltioApi
) : IAuthRepository {

    override suspend fun login(loginRequest: LoginRequest): AuthResponse {
        return api.login(loginRequest)
    }

    override suspend fun register(authRequest: AuthRequest): AuthResponse {
        return api.register(authRequest)
    }

    override suspend fun registerCompany(request: CompanyRegisterRequest, logoBytes: ByteArray?): AuthResponse {
        if (logoBytes != null) {
            val textType = "text/plain".toMediaTypeOrNull()
            val logoPart = logoBytes.let {
                val requestFile = it.toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("logo", "company_logo.jpg", requestFile)
            }
            return api.registerCompanyWithLogo(
                name = request.name.toRequestBody(textType),
                lastname = request.lastname.toRequestBody(textType),
                email = request.email.toRequestBody(textType),
                password = request.password.toRequestBody(textType),
                phone = request.phone.toRequestBody(textType),
                commercialName = request.commercialName.toRequestBody(textType),
                address = request.address.toRequestBody(textType),
                latitude = request.latitude.toString().toRequestBody(textType),
                longitude = request.longitude.toString().toRequestBody(textType),
                contactPhone = request.contactPhone.toRequestBody(textType),
                contactEmail = request.contactEmail.toRequestBody(textType),
                role = request.role.toRequestBody(textType),
                accountType = request.accountType.toRequestBody(textType),
                logo = logoPart
            )
        }
        return api.registerCompany(request)
    }

    override suspend fun registerDelivery(request: DeliveryRegisterRequest): AuthResponse {
        return api.registerDelivery(request)
    }

    override suspend fun googleAuth(idToken: String): AuthResponse {
        return api.googleAuth(GoogleAuthRequest(idToken))
    }

    override suspend fun googleAuthProgresive(request: GoogleAuthRequest): AuthResponse {
        return api.googleAuthProgresive(request)
    }

    override suspend fun logout(): MessageResponse {
        return api.logout()
    }

    override suspend fun refreshToken(): MessageResponse {
        return api.refreshToken()
    }

    override suspend fun getProfile(): ProfileResponse {
        return api.getProfile()
    }

    override suspend fun verifyToken(): ProfileResponse {
        return api.verifyToken()
    }

    override suspend fun registerFCMToken(token: String, userId: Int, fcmToken: String): MessageResponse {
        return api.registerFCMToken("Bearer $token", RegisterFCMTokenRequest(userId, fcmToken))
    }

    override suspend fun getCompanyById(id: Int): Company {
        val dto = api.getCompanyById(id)
        return Company(
            id = dto.id,
            userId = dto.userId,
            commercialName = dto.commercialName,
            address = dto.address ?: "",
            latitude = dto.latitude ?: 0.0,
            longitude = dto.longitude ?: 0.0,
            contactPhone = dto.contactPhone ?: "",
            contactEmail = dto.contactEmail ?: "",
            logoUrl = dto.logoUrl,
            registrationDate = dto.registrationDate ?: "",
            totalProducts = dto.totalProducts ?: 0
        )
    }

    override suspend fun getCompanyByUserId(token: String, userId: Int): Company {
        val dto = api.getCompanyByUserId(userId)
        return Company(
            id = dto.id,
            userId = dto.userId,
            commercialName = dto.commercialName,
            address = dto.address ?: "",
            latitude = dto.latitude ?: 0.0,
            longitude = dto.longitude ?: 0.0,
            contactPhone = dto.contactPhone ?: "",
            contactEmail = dto.contactEmail ?: "",
            logoUrl = dto.logoUrl,
            registrationDate = dto.registrationDate ?: "",
            totalProducts = dto.totalProducts ?: 0
        )
    }

    override suspend fun getCompanyProducts(id: Int): List<Product> {
        return api.getCompanyProducts(id).map { it.toDomain() }
    }
}
