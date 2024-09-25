package com.happy.friendogly.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class ChatAlarmModule(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    private val key = booleanPreferencesKey(ALARM_SETTING)

    var isSet: Flow<Boolean> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[key] ?: true
        }

    suspend fun saveSetting(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun deleteSetting() {
        context.dataStore.edit { prefs ->
            prefs.remove(key)
        }
    }

    companion object {
        private const val ALARM_SETTING = "CHAT_ALARM_SETTING"
        private const val DATA_STORE_NAME = "chatAlarmDataStore"
    }
}
