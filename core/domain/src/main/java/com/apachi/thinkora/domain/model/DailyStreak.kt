package com.apachi.thinkora.domain.model

data class DailyStreak(
    val currentStreak: Int,
    val lastReadDate: Long // Epoch millis
)
