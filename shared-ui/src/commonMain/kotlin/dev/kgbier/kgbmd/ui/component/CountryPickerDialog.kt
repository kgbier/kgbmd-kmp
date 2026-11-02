@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.kgbier.kgbmd.domain.model.Country

@Composable
fun CountryPickerDialog(
    onDismissRequest: () -> Unit,
    initialValue: Country? = null,
    onSubmitted: (Country?) -> Unit = {},
) {
    var countryState: Country? by remember { mutableStateOf(initialValue) }

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
                val usa = Country("US")
                val aus = Country("AU")
                ListItem(
                    selected = countryState == usa,
                    onClick = { countryState = usa },
                ) {
                    Text("🇺🇸 America")
                }
                ListItem(
                    selected = countryState == aus,
                    onClick = { countryState = aus },
                ) {
                    Text("🇦🇺 Australia")
                }
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
                            onSubmitted(countryState)
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
fun CountryPickerDialogPreview() {
    CountryPickerDialog(
        onDismissRequest = {},
    )
}
