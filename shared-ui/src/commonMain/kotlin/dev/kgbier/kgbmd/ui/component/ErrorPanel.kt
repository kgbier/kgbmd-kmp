@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.ui.component.ErrorMessage.Type
import io.ktor.util.network.UnresolvedAddressException

data class ErrorMessage(
    val type: Type = Type.General,
    val title: String? = null,
    val message: String? = null,
) {
    enum class Type {
        Network,
        General,
    }
}

fun Throwable.toErrorMessage(): ErrorMessage {
    val type = when (this) {
        is UnresolvedAddressException -> Type.Network
        else -> Type.General
    }

    val title = when (this) {
        is UnresolvedAddressException -> "Could not connect"
        else -> null
    }

    val message = when (this) {
        is UnresolvedAddressException -> "No connection found. Check your connectivity and try again."
        else -> null
    }

    return ErrorMessage(
        type = type,
        title = title,
        message = message,
    )
}

@Composable
fun ErrorPanel(
    errorMessage: ErrorMessage,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onPrimaryActionClick: (() -> Unit) = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(contentPadding)
    ) {
        Icon(
            imageVector = when (errorMessage.type) {
                Type.Network -> Icons.Outlined.ErrorOutline
                Type.General -> Icons.Outlined.ErrorOutline
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.defaultMinSize(
                minWidth = 64.dp,
                minHeight = 64.dp,
            )
        )
        Text(
            text = errorMessage.title ?: "Error",
            style = MaterialTheme.typography.titleLarge,
        )
        errorMessage.message?.let {
            Text(text = it)
        }
        Button(
            onClick = onPrimaryActionClick,
        ) {
            Text("Retry")
        }
    }
}

@Preview
@Composable
fun ErrorPanelPreview() = MaterialTheme {
    ErrorPanel(
        errorMessage = ErrorMessage(
            title = "Error Title",
            message = "Error message goes here."
        )
    )
}
