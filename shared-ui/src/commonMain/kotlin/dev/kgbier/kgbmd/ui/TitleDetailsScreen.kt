package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.TitleDetails
import dev.kgbier.kgbmd.ui.component.CastCreditRow
import dev.kgbier.kgbmd.ui.component.molecule.TitledContent
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute

@Composable
fun TitleDetailsScreen(
    title: TitleDetails,
    router: Router<AppRoute>,
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    TitleDetailsHeader(
        title = title,
        modifier = Modifier
            .padding(16.dp)
            .padding(
                top = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
    )

    TitlePrincipalCredits(
        title = title,
        router = router,
        modifier = Modifier.fillMaxWidth()
    )

    title.description?.let { description ->
        TitledContent(
            title = "Summary",
            body = description,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    val topCast = title.topCast
    if (topCast != null) {
        Row {
            Text(
                text = "Top Cast",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Column {
            for (castCredit in topCast.topCast) {
                CastCreditRow(
                    name = castCredit.nameProfile.name,
                    roles = castCredit.roles,
                    creditImageUrl = castCredit.nameProfile.photo?.thumbnailUrl,
                    onClick = { router.push(AppRoute.Details(castCredit.nameProfile.id)) },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}