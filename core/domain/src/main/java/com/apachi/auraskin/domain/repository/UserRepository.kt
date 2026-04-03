package com.apachi.auraskin.domain.repository

import com.apachi.auraskin.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserPreferences(): Flow<UserPreferences?>
    suspend fun saveUserPreferences(userPreferences: UserPreferences)
    suspend fun clearUserData()
}
