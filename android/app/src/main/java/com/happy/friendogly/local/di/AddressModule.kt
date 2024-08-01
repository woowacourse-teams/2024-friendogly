package com.happy.friendogly.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AddressModule(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    private val key = stringPreferencesKey(KEY_MY_ADDRESS)

    var myAddress: Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[key] ?: ""
        }

    suspend fun saveLocation(location: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = location
        }
    }

    companion object {
        private const val KEY_MY_ADDRESS = "myAddress"
        private const val DATA_STORE_NAME = "addressDataStore"
    }
}
