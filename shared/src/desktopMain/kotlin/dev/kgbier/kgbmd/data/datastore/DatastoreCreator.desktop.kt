package dev.kgbier.kgbmd.data.datastore

import androidx.datastore.core.FileStorage
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import java.io.File

actual class PlatformDatastoreCreator {
    actual fun createStorage(filename: String): Storage<Preferences> = FileStorage(
        serializer = PreferencesFileSerializer,
        produceFile = { File(System.getProperty("java.io.tmpdir"), filename) }
    )
}
