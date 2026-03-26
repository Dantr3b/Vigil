package com.vigil.app.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val activeProfileUid: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ACTIVE_PROFILE_UID]
    }

    suspend fun setActiveProfileUid(profileUid: String?) {
        dataStore.edit { preferences ->
            if (profileUid == null) {
                preferences.remove(ACTIVE_PROFILE_UID)
            } else {
                preferences[ACTIVE_PROFILE_UID] = profileUid
            }
        }
    }

    companion object {
        val ACTIVE_PROFILE_UID = stringPreferencesKey("active_profile_uid")
    }
}
