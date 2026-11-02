package dev.kgbier.kgbmd.domain.model

import kotlin.jvm.JvmInline

/**
 * ISO 3166, in its Alpha-2 code representation.
 * e.g. `US`, `AU`, `CA`.
 */
@JvmInline
value class Country(val alpha2: String)
