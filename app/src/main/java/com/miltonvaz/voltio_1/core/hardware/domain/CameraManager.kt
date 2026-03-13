package com.miltonvaz.voltio_1.core.hardware.domain

interface CameraManager {
    fun createTempImageUri(): android.net.Uri
    fun readImageBytes(uri: android.net.Uri): ByteArray?
}