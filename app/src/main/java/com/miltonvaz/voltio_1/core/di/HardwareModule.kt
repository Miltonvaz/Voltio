package com.miltonvaz.voltio_1.core.di

import com.miltonvaz.voltio_1.core.hardware.data.AndroidAuthManager
import com.miltonvaz.voltio_1.core.hardware.data.AndroidCameraManager
import com.miltonvaz.voltio_1.core.hardware.data.AndroidVibrationManager
import com.miltonvaz.voltio_1.core.hardware.domain.AuthManager
import com.miltonvaz.voltio_1.core.hardware.domain.VibrationManager
import com.miltonvaz.voltio_1.core.hardware.domain.CameraManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {
    @Binds
    @Singleton
    abstract fun bindVibrationManager(
        impl: AndroidVibrationManager
    ): VibrationManager

    @Binds
    @Singleton
    abstract fun bindAuthManager(
        impl: AndroidAuthManager
    ): AuthManager

    @Binds
    @Singleton
    abstract fun bindCameraManager(
        impl: AndroidCameraManager
    ): CameraManager
}
