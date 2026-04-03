package com.apachi.auraskin.domain.model

enum class HabitCategory {
    SKINCARE_MORNING,
    SKINCARE_NIGHT,
    LIFESTYLE,
    DIET
}

data class Habit(
    val id: String,
    val name: String,
    val category: HabitCategory = HabitCategory.LIFESTYLE,
    val streak: Int,
    val createdTimestamp: Long
)
