package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.AchievementType
import com.apachi.auraskin.domain.repository.GamificationRepository
import com.apachi.auraskin.domain.repository.HabitRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckAchievementsUseCase @Inject constructor(
    private val gamificationRepository: GamificationRepository,
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke() {
        val habits = habitRepository.getAllHabits().first()
        val achievements = gamificationRepository.getAchievements().first()
        val lockedAchievements = achievements.filter { !it.isUnlocked }

        if (lockedAchievements.isEmpty()) return

        // Calculate stats
        val habitCount = habits.size
        val maxStreak = habits.maxOfOrNull { it.streak } ?: 0

        lockedAchievements.forEach { achievement ->
            val isMet = when (achievement.type) {
                AchievementType.HABIT_COUNT -> habitCount >= achievement.threshold
                AchievementType.STREAK -> maxStreak >= achievement.threshold
            }

            if (isMet) {
                gamificationRepository.unlockAchievement(achievement.id)
            }
        }
    }
}
