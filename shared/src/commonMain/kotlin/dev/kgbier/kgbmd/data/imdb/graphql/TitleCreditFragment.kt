package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.data.imdb.model.transformImageUrl
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.NameProfile
import kotlinx.serialization.Serializable

@Serializable
data class NameProfileCreditFragment(
    val id: String,
    val nameText: NameText,
    val primaryImage: PrimaryImage?,
) {
    @Serializable
    data class NameText(val text: String)

    @Serializable
    data class PrimaryImage(val url: String?)


    companion object {
        const val name = "NameProfileCredit"
        const val fragment = """
fragment $name on CreditV2 {
  name {
    id
    nameText {
      text
    }
    primaryImage {
      url
    }
  }
}
"""
    }
}

fun NameProfileCreditFragment.toNameProfile() = NameProfile(
    id = MediaEntityId(id),
    name = nameText.text,
    photo = primaryImage?.url?.let(::transformImageUrl),
)
