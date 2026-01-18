package dev.kgbier.kgbmd

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.kgbier.kgbmd.ui.MainScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "kgbmd",
    ) {
        MainScreen()
    }
}