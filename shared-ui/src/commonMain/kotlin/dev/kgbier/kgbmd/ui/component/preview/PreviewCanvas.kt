package dev.kgbier.kgbmd.ui.component.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider

annotation class AppPreview {

    @PreviewLightDark
    @PreviewWrapper(ComponentWrapper::class)
    annotation class Component

    @PreviewLightDark
    @PreviewWrapper(ScreenWrapper::class)
    annotation class Screen
}

class ComponentWrapper : PreviewWrapperProvider {
    private val themeWrapper = ThemeWrapper()
    private val surfaceWrapper = SurfaceWrapper()

    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        // Nest the wrappers: Theme is usually the outermost layer,
        // followed by the environment/container wrapper.
        themeWrapper.Wrap { surfaceWrapper.Wrap { content() } }
    }
}

class ScreenWrapper : PreviewWrapperProvider {
    private val themeWrapper = ThemeWrapper()
    private val scaffoldWrapper = ScaffoldWrapper()

    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        // Nest the wrappers: Theme is usually the outermost layer,
        // followed by the environment/container wrapper.
        themeWrapper.Wrap { scaffoldWrapper.Wrap { content() } }
    }
}
