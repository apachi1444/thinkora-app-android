package com.apachi.thinkora.data.repository

import com.apachi.thinkora.data.local.dao.AchievementDao
import com.apachi.thinkora.data.local.entity.toDomain
import com.apachi.thinkora.domain.repository.GamificationRepository
import com.apachi.thinkora.domain.model.Achievement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GamificationRepositoryImpl @Inject constructor(
    private val achievementDao: AchievementDao
) : GamificationRepository {

    override fun getAchievements(): Flow<List<Achievement>> {
        return achievementDao.getAchievements().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun unlockAchievement(achievementId: String) {
        val currentTime = System.currentTimeMillis()
        achievementDao.unlockAchievement(achievementId, currentTime)
    }
}
