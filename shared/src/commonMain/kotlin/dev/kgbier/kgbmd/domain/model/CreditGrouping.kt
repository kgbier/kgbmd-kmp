package dev.kgbier.kgbmd.domain.model

data class CreditGrouping(
    val id: CreditGroupingId,
    val name: String,
    val count: Int,
)
