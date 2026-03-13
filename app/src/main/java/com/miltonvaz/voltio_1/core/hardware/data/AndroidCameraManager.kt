package com.miltonvaz.voltio_1.core.hardware.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.miltonvaz.voltio_1.core.hardware.domain.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import jakarta.inject.Inject

class AndroidCameraManager @Inject constructor(
    @ApplicationContext private val context: Context
) : CameraManager {

    override fun createTempImageUri(): Uri {
        val photoFile = File.createTempFile("photo_", ".jpg", context.externalCacheDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    override fun readImageBytes(uri: Uri): ByteArray? {
        return context.contentResolver.openInputStream(uri)?.readBytes()
    }
}