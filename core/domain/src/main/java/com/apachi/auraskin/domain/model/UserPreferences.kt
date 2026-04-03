package com.apachi.auraskin.domain.model

data class UserPreferences(
    val userName: String = "",
    val skinFocus: String = "",
    val skinType: String = "",
    val routineType: String = "",
    val isOnboardingCompleted: Boolean = false
)
