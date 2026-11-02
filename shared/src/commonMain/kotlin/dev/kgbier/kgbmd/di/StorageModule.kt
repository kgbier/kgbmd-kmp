package dev.kgbier.kgbmd.di

import dev.kgbier.kgbmd.data.PreferencesDatastore
import dev.kgbier.kgbmd.data.datastore.DatastoreCreator
import dev.kgbier.kgbmd.data.datastore.PlatformDatastoreCreator
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class StorageModule(coreModule: CoreModule) {

    val platformModule = StoragePlatformModule(coreModule)

    val datastoreCreator by lazy {
        DatastoreCreator(
            coroutineContext = coreModule.coroutinesModule.rootContext + Dispatchers.IO,
            platformCreator = platformModule.platformDatastoreCreator,
        )
    }

    val kgbmdPreferencesDatastore by lazy {
        datastoreCreator.createDatastore()
    }

    val preferencesRepo: PreferencesRepo by lazy {
        PreferencesRepo(
            prefsService = PreferencesDatastore(
                datastore = kgbmdPreferencesDatastore,
            )
        )
    }
}

expect class StoragePlatformModule(
    coreModule: CoreModule,
) {
    val platformDatastoreCreator: PlatformDatastoreCreator
}
