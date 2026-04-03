package com.apachi.auraskin.domain.repository

import com.apachi.auraskin.domain.model.Achievement
import kotlinx.coroutines.flow.Flow

interface GamificationRepository {
    fun getAchievements(): Flow<List<Achievement>>
    suspend fun unlockAchievement(achievementId: String)
}
