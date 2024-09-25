package com.happy.friendogly.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.happy.friendogly.data.model.UserAddressDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddressModule @Inject constructor(
    @ApplicationContext
    val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

    private val keyAdmin = stringPreferencesKey(KEY_ADMIN)
    private val keyLocality = stringPreferencesKey(KEY_SUB_LOCALITY)
    private val keyThoroughfare = stringPreferencesKey(KEY_THOROUGHFARE)

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

    var thoroughfare: Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[keyThoroughfare] ?: ""
        }

    suspend fun saveAddress(userAddressDto: UserAddressDto) {
        context.dataStore.edit { preferences ->
            preferences[keyThoroughfare] = userAddressDto.thoroughfare ?: ""
            preferences[keyLocality] = userAddressDto.subLocality ?: ""
            preferences[keyAdmin] = userAddressDto.adminArea
        }
    }

    suspend fun deleteAddressData() {
        context.dataStore.edit { prefs ->
            prefs.remove(keyAdmin)
            prefs.remove(keyLocality)
            prefs.remove(keyThoroughfare)
        }
    }

    companion object {
        private const val KEY_THOROUGHFARE = "thoroughfare"
        private const val KEY_SUB_LOCALITY = "subLocality"
        private const val KEY_ADMIN = "adminArea"
        private const val DATA_STORE_NAME = "addressDataStore"
    }
}
