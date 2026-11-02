package dev.kgbier.kgbmd.ui.di

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.kgbier.kgbmd.di.CoreModule
import dev.kgbier.kgbmd.di.CorePlatformModule

@Composable
actual fun rememberRootModule(): RootModule {
    val context = LocalContext.current
    val activity = LocalActivity.current

    return remember(context, activity) {
        RootModule(
            CoreModule(
                CorePlatformModule(
                    applicationContext = context,
                    activityContext = requireNotNull(activity) { "Activity Context required" },
                )
            )
        )
    }
}
