package com.apachi.auraskin.data.local

import androidx.room.TypeConverter
import com.apachi.auraskin.domain.model.AchievementType

class Converters {
    @TypeConverter
    fun fromAchievementType(value: AchievementType): String {
        return value.name
    }

    @TypeConverter
    fun toAchievementType(value: String): AchievementType {
        return AchievementType.valueOf(value)
    }
}
