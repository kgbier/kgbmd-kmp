package dev.kgbier.kgbmd.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStore.Builder
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import kotlin.coroutines.CoroutineContext

class DatastoreCreator(
    val dataStoreFileName: String = "kgbmd.preferences_pb",
    val coroutineContext: CoroutineContext,
    val platformCreator: PlatformDatastoreCreator,
) {

    fun createDatastore(): DataStore<Preferences> = Builder(
        storage = platformCreator.createStorage(filename = dataStoreFileName),
        context = coroutineContext,
    ).build()
}

expect class PlatformDatastoreCreator {
    fun createStorage(filename: String): Storage<Preferences>
}
