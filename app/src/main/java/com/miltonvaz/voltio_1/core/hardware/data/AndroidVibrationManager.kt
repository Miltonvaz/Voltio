package com.miltonvaz.voltio_1.core.hardware.data
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.miltonvaz.voltio_1.core.hardware.domain.VibrationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class AndroidVibrationManager @Inject constructor(
    @ApplicationContext private val context: Context
) : VibrationManager {

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun vibrate(durationMillis: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMillis)
        }
    }

    override fun cancel() {
        vibrator.cancel()
    }
}