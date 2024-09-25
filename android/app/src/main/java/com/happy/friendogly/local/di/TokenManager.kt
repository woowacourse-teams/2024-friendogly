package com.happy.friendogly.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class TokenManager constructor(
    val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    private val keyAccessToken = stringPreferencesKey(KEY_ACCESS_TOKEN)
    private val keyRefreshToken = stringPreferencesKey(KEY_REFRESH_TOKEN)

    var accessToken: Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[keyAccessToken] ?: ""
        }

    var refreshToken: Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[keyRefreshToken] ?: ""
        }

    suspend fun saveAccessToken(value: String) {
        context.dataStore.edit { preferences ->
            preferences[keyAccessToken] = value
        }
    }

    suspend fun saveRefreshToken(value: String) {
        context.dataStore.edit { preferences ->
            preferences[keyRefreshToken] = value
        }
    }

    suspend fun deleteToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(keyAccessToken)
            prefs.remove(keyRefreshToken)
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
        private const val DATA_STORE_NAME = "dataStore"
    }
}
