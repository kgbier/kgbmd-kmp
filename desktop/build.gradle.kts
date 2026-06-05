import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    jvmToolchain(21)
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