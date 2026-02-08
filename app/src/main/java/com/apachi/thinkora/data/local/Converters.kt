package com.apachi.thinkora.data.local

import androidx.room.TypeConverter
import com.apachi.thinkora.domain.model.AchievementType

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
