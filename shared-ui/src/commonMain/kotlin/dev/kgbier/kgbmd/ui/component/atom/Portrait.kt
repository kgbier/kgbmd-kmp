package dev.kgbier.kgbmd.ui.component.atom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest

@Composable
fun Portrait(
    creditImageUrl: String? = null,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier
        .clip(CircleShape)
) {
    val contentScale = ContentScale.Crop

    val sizeResolver = rememberConstraintsSizeResolver()
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(creditImageUrl)
            .size(sizeResolver)
            .build(),
        contentScale = contentScale,
    )
    val painterState by painter.state.collectAsState()
    when (painterState) {
        is AsyncImagePainter.State.Success -> Image(
            painter = painter,
            contentDescription = null,
            contentScale = contentScale,
            alignment = BiasAlignment(0f, -0.62f),
            modifier = Modifier
                .matchParentSize()
                .then(sizeResolver)
        )

        else -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .matchParentSize()
                .then(sizeResolver)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxSize(0.62f)
            )
        }
    }
}

@Preview
@Composable
private fun PortraitPlaceholderPreview() = MaterialTheme {
    Portrait(
        creditImageUrl = null,
        modifier = Modifier.size(64.dp)
    )
}
