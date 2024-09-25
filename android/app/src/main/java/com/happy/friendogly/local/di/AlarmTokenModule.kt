package com.happy.friendogly.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AlarmTokenModule
    @Inject
    constructor(
        @ApplicationContext
        val context: Context,
    ) {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

        private val key = stringPreferencesKey(FCM_TOKEN)

        var token: Flow<String> =
            context.dataStore.data.catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[key] ?: ""
            }

        suspend fun saveToken(value: String) {
            context.dataStore.edit { preferences ->
                preferences[key] = value
            }
        }

        suspend fun deleteToken() {
            context.dataStore.edit { prefs ->
                prefs.remove(key)
            }
        }

        companion object {
            private const val FCM_TOKEN = "FCM_TOKEN"
            private const val DATA_STORE_NAME = "fcmDataStore"
        }
    }
