package com.apachi.thinkora.domain.model

enum class AchievementType {
    STREAK,
    HABIT_COUNT
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String, // e.g., "ic_star", "ic_fire"
    val type: AchievementType,
    val threshold: Int,
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null
)
