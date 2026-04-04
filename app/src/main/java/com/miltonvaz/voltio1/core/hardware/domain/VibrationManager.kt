package com.miltonvaz.voltio1.core.hardware.domain

interface VibrationManager {
    fun vibrate(durationMillis: Long = 500)
    fun cancel()
}