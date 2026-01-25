package com.plcoding.widgetswithcompose.domain.repository

import com.plcoding.widgetswithcompose.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserPreferences(): Flow<UserPreferences?>
    suspend fun saveUserPreferences(userPreferences: UserPreferences)
}
