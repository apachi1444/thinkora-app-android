package com.apachi.auraskin.data.repository

import com.apachi.auraskin.data.local.dao.AchievementDao
import com.apachi.auraskin.data.local.entity.toDomain
import com.apachi.auraskin.domain.repository.GamificationRepository
import com.apachi.auraskin.domain.model.Achievement
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
