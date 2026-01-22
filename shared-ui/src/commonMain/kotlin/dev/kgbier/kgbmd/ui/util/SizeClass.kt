package dev.kgbier.kgbmd.ui.util

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

const val ExpandedWidth = 840
const val MediumWidth = 600

enum class SizeClass {
    Compact,
    Medium,
    Expanded,
    ;

    inline val isCompact: Boolean get() = this == Compact
    inline val isMedium: Boolean get() = this == Medium
    inline val isExpanded: Boolean get() = this == Expanded
}

val LocalSizeClass = staticCompositionLocalOf { SizeClass.Compact }

fun BoxWithConstraintsScope.computeSizeClass(): SizeClass = when {
    maxWidth >= ExpandedWidth.dp -> SizeClass.Expanded
    maxWidth >= MediumWidth.dp -> SizeClass.Medium
    else -> SizeClass.Compact
}
