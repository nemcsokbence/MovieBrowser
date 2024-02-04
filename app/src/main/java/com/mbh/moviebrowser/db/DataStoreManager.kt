package com.mbh.moviebrowser.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

interface DataStoreManager {
    suspend fun getLastSyncTimestamp(): Long?
    suspend fun saveLastSyncTimestamp(timestamp: Long)
}

class DataStoreManagerImpl(private val context: Context): DataStoreManager {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATA_STORE_NAME
    )

    private val timestampKey = longPreferencesKey(TIMESTAMP)
    override suspend fun getLastSyncTimestamp(): Long? {
        return context.dataStore.data.map {
            it[timestampKey]
        }.firstOrNull()
    }

    override suspend fun saveLastSyncTimestamp(timestamp: Long) {
        context.dataStore.edit {
            it[timestampKey] = timestamp
        }
    }

}

const val DATA_STORE_NAME = "MOVIE_BROWSER_DATA_STORE"
const val TIMESTAMP = "TIMESTAMP"