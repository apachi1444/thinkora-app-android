package com.apachi.thinkora.domain.repository

import com.apachi.thinkora.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserPreferences(): Flow<UserPreferences?>
    suspend fun saveUserPreferences(userPreferences: UserPreferences)
}
