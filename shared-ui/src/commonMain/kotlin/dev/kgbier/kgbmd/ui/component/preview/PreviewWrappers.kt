package dev.kgbier.kgbmd.ui.component.preview

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import dev.kgbier.kgbmd.ui.theme.AppTheme

class ThemeWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) = AppTheme {
        content()
    }
}

class SurfaceWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) = Surface {
        content()
    }
}

class ScaffoldWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) = Scaffold {
        content()
    }
}
