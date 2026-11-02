package dev.kgbier.kgbmd.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

context(datastore: DataStore<Preferences>)
inline fun <reified T> Preferences.Key<T>.flow(): Flow<T?> = datastore.data.map { preferences ->
    preferences[this]
}

context(datastore: DataStore<Preferences>)
suspend inline fun <reified T> Preferences.Key<T>.set(value: T?) {
    datastore.edit { preferences ->
        when (value) {
            null -> preferences.remove(this)
            else -> preferences[this] = value
        }
    }
}

// Class-Context bridging
// https://youtrack.jetbrains.com/issue/KT-85022/Support-context-modifier-on-val-var-object-declarations-as-context-parameter-providers

/**
 * A temporary holder to improve ergonomics with context parameters.
 * [DatastorePreferencesHolder] is not intended to be part of any public interface.
 */
interface DatastorePreferencesHolder {
    val datastore: DataStore<Preferences>
}

context(holder: DatastorePreferencesHolder)
inline fun <reified T> Preferences.Key<T>.flow() = context(holder.datastore) {
    flow(datastore = holder.datastore)
}

context(holder: DatastorePreferencesHolder)
suspend inline fun <reified T> Preferences.Key<T>.set(value: T?) {
    context(holder.datastore) {
        set(datastore = holder.datastore, value = value)
    }
}
