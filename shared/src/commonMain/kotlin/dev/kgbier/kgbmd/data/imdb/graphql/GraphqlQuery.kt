package dev.kgbier.kgbmd.data.imdb.graphql

interface GraphqlQuery<Params, Result> {
    val document: String
}