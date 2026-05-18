package dev.kgbier.kgbmd.ui.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

fun LazyListScope.itemSpacing(spacing: Dp) = item {
    Spacer(Modifier.size(spacing))
}
