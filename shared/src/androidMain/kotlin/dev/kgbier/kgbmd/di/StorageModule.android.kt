package dev.kgbier.kgbmd.di

import dev.kgbier.kgbmd.data.datastore.PlatformDatastoreCreator

actual class StoragePlatformModule actual constructor(coreModule: CoreModule) {
    actual val platformDatastoreCreator: PlatformDatastoreCreator by lazy {
        PlatformDatastoreCreator(coreModule.platformModule.applicationContext)
    }
}
