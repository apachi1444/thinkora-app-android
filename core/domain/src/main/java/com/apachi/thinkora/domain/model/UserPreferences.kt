package com.apachi.thinkora.domain.model

data class UserPreferences(
    val userName: String,
    val interestCategories: List<String>,
    val isOnboardingCompleted: Boolean
)
