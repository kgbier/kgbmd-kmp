package dev.kgbier.kgbmd.di

import android.content.Context

actual class CorePlatformModule(
    val applicationContext: Context,
    val activityContext: Context,
)
