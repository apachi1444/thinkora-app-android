package com.plcoding.widgetswithcompose.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.plcoding.widgetswithcompose.domain.model.UserPreferences
import com.plcoding.widgetswithcompose.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserRepository {

    private val USER_NAME = stringPreferencesKey("user_name")
    private val INTERESTS = stringSetPreferencesKey("interests")
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    override fun getUserPreferences(): Flow<UserPreferences?> {
        return context.dataStore.data.map { prefs ->
            val isOnboardingCompleted = prefs[ONBOARDING_COMPLETED] ?: false
            if (isOnboardingCompleted) {
                UserPreferences(
                    userName = prefs[USER_NAME] ?: "",
                    interestCategories = prefs[INTERESTS]?.toList() ?: emptyList(),
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
            prefs[INTERESTS] = userPreferences.interestCategories.toSet()
            prefs[ONBOARDING_COMPLETED] = userPreferences.isOnboardingCompleted
        }
    }
}
