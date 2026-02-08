package com.apachi.thinkora.domain.repository

import com.apachi.thinkora.domain.model.Achievement
import kotlinx.coroutines.flow.Flow

interface GamificationRepository {
    fun getAchievements(): Flow<List<Achievement>>
    suspend fun unlockAchievement(achievementId: String)
}
