
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.secrets.gradle) apply false                  // Secrets Gradle
    alias(libs.plugins.jetbrainsKotlinSerialization) apply false    // Serialization
    alias(libs.plugins.hilt.android) apply false                    // Hilt
    alias(libs.plugins.devtools.ksp) apply false
}