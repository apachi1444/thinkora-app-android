package com.apachi.auraskin.domain.model

data class DailyStreak(
    val currentStreak: Int,
    val lastReadDate: Long // Epoch millis
)
