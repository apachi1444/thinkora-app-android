package com.apachi.thinkora.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apachi.thinkora.domain.model.Achievement
import com.apachi.thinkora.domain.model.AchievementType

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val type: AchievementType,
    val threshold: Int,
    val isUnlocked: Boolean,
    val unlockedDate: Long?
)

fun AchievementEntity.toDomain(): Achievement {
    return Achievement(
        id = id,
        title = title,
        description = description,
        iconName = iconName,
        type = type,
        threshold = threshold,
        isUnlocked = isUnlocked,
        unlockedDate = unlockedDate
    )
}

fun Achievement.toEntity(): AchievementEntity {
    return AchievementEntity(
        id = id,
        title = title,
        description = description,
        iconName = iconName,
        type = type,
        threshold = threshold,
        isUnlocked = isUnlocked,
        unlockedDate = unlockedDate
    )
}
