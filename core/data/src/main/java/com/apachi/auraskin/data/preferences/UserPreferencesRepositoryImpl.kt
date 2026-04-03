package com.apachi.auraskin.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.apachi.auraskin.domain.model.UserPreferences
import com.apachi.auraskin.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserRepository {

    private val USER_NAME = stringPreferencesKey("user_name")
    private val SKIN_FOCUS = stringPreferencesKey("skin_focus")
    private val SKIN_TYPE = stringPreferencesKey("skin_type")
    private val ROUTINE_TYPE = stringPreferencesKey("routine_type")
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    override fun getUserPreferences(): Flow<UserPreferences?> {
        return context.dataStore.data.map { prefs ->
            val isOnboardingCompleted = prefs[ONBOARDING_COMPLETED] ?: false
            if (isOnboardingCompleted) {
                UserPreferences(
                    userName = prefs[USER_NAME] ?: "",
                    skinFocus = prefs[SKIN_FOCUS] ?: "",
                    skinType = prefs[SKIN_TYPE] ?: "",
                    routineType = prefs[ROUTINE_TYPE] ?: "",
                    isOnboardingCompleted = true
                )
            } else {
                null
            }
        }
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = userPreferences.userName
            prefs[SKIN_FOCUS] = userPreferences.skinFocus
            prefs[SKIN_TYPE] = userPreferences.skinType
            prefs[ROUTINE_TYPE] = userPreferences.routineType
            prefs[ONBOARDING_COMPLETED] = userPreferences.isOnboardingCompleted
        }
    }

    override suspend fun clearUserData() {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_NAME)
            prefs.remove(SKIN_FOCUS)
            prefs.remove(SKIN_TYPE)
            prefs.remove(ROUTINE_TYPE)
            prefs[ONBOARDING_COMPLETED] = false
        }
    }
}
