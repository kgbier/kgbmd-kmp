package dev.kgbier.kgbmd.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.kgbier.kgbmd.di.CoreModule
import dev.kgbier.kgbmd.di.CorePlatformModule

@Composable
actual fun rememberRootModule(): RootModule =
    remember { RootModule(CoreModule(CorePlatformModule())) }
