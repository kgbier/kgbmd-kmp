import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.sharedUi)

    implementation(libs.logback)

    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "dev.kgbier.kgbmd.MainKt"
        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Exe,
            )
            jvmArgs(
                "-Dapple.awt.application.appearance=system"
            )
        }
    }
}