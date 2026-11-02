package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TextFieldDialog(
    onDismissRequest: () -> Unit,
    initialValue: String = "",
    onSubmitted: (String) -> Unit = {},
    headlineContent: @Composable () -> Unit = {},
    placeholderContent: @Composable () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                headlineContent()
                Spacer(modifier = Modifier.size(16.dp))
                val state = rememberTextFieldState(initialText = initialValue)
                TextField(
                    state = state,
                    placeholder = placeholderContent,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        },
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            onSubmitted(state.text.toString())
                            onDismissRequest()
                        },
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
@Preview(widthDp = 512, heightDp = 512)
fun TextFieldDialogPreview() {
    TextFieldDialog(
        onDismissRequest = {},
        headlineContent = { Text("Enter text") },
        placeholderContent = { Text("Placeholder") },
    )
}
