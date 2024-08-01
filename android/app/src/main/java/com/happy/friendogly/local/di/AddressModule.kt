package com.happy.friendogly.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.happy.friendogly.data.model.AddressDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AddressModule(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    private val keyAddress = stringPreferencesKey(KEY_ADDRESS)
    private val keyLocality = stringPreferencesKey(KEY_SUB_LOCALITY)
    private val keyAdmin = stringPreferencesKey(KEY_ADMIN)

    var address: Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[keyAddress] ?: ""
        }

    var subLocality: Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[keyLocality] ?: ""
        }

    var adminArea: Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[keyAdmin] ?: ""
        }


    suspend fun saveAddress(addressDto: AddressDto) {
        context.dataStore.edit { preferences ->
            preferences[keyAddress] = addressDto.address
            preferences[keyLocality] = addressDto.subLocality
            preferences[keyAdmin] = addressDto.adminArea
        }
    }

    suspend fun deleteAddressData() {
        context.dataStore.edit { prefs ->
            prefs.remove(keyAdmin)
            prefs.remove(keyLocality)
            prefs.remove(keyAddress)
        }
    }

    companion object {
        private const val KEY_ADDRESS = "myAddress"
        private const val KEY_SUB_LOCALITY = "subLocality"
        private const val KEY_ADMIN = "adminArea"
        private const val DATA_STORE_NAME = "addressDataStore"
    }
}
