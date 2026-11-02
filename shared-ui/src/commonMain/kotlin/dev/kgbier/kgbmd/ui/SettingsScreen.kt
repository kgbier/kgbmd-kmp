@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kgbier.kgbmd.domain.model.Country
import dev.kgbier.kgbmd.presentation.SettingsScreenViewModel
import dev.kgbier.kgbmd.ui.component.CountryPickerDialog
import dev.kgbier.kgbmd.ui.component.TextFieldDialog
import dev.kgbier.kgbmd.ui.component.preview.AppPreview
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.util.ExpandedWidth
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingsScreen(
    viewModelFactory: SettingsScreenViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: SettingsScreenViewModel = remember {
        viewModelFactory.createSettingsScreenViewModelFactory(scope)
    }
) {
    val country by viewModel.country.collectAsStateWithLifecycle()
    val postcode by viewModel.postcode.collectAsStateWithLifecycle()

    SettingsScreen(
        country = country,
        postcode = postcode,
        onPostcodeUpdated = viewModel::updatePostcode,
        onCountryUpdated = viewModel::updateCountry,
    )
}

@Composable
fun SettingsScreen(
    country: Country?,
    postcode: String?,
    onPostcodeUpdated: (String) -> Unit = {},
    onCountryUpdated: (Country?) -> Unit = {},
) = Scaffold(
    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = ExpandedWidth.dp)
        ) {
            val countryDialog = remember { mutableStateOf(false) }
            val postcodeDialog = remember { mutableStateOf(false) }

            when {
                countryDialog.value -> CountryPickerDialog(
                    onDismissRequest = { countryDialog.value = false },
                    initialValue = country,
                    onSubmitted = onCountryUpdated,
                )

                postcodeDialog.value -> TextFieldDialog(
                    onDismissRequest = { postcodeDialog.value = false },
                    initialValue = postcode ?: "",
                    onSubmitted = onPostcodeUpdated,
                    headlineContent = @Composable { Text(text = "Enter your postcode") },
                    placeholderContent = @Composable { Text("1234") },
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Location",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ListItem(
                    onClick = { countryDialog.value = true },
                    supportingContent = country?.let { { Text(it.alpha2) } }
                ) {
                    Text("Country")
                }
                ListItem(
                    onClick = { postcodeDialog.value = true },
                    supportingContent = postcode?.let { { Text(it) } }
                ) {
                    Text("Postcode")
                }
            }
        }
    }
}

@Composable
@AppPreview.Screen
fun SettingsScreenPreview() {
    SettingsScreen(
        country = null,
        postcode = "3000",
    )
}
