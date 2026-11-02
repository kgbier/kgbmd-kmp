package dev.kgbier.kgbmd.data.datastore

import android.content.Context
import androidx.datastore.core.FileStorage
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer

actual class PlatformDatastoreCreator(
    val context: Context,
) {
    actual fun createStorage(filename: String): Storage<Preferences> = FileStorage(
        serializer = PreferencesFileSerializer,
        produceFile = { context.filesDir.resolve(filename) }
    )
}
