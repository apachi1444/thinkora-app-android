package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.UserPreferences
import com.apachi.auraskin.domain.repository.UserRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, skinFocus: String, skinType: String, routineType: String) {
        val preferences = UserPreferences(
            userName = name,
            skinFocus = skinFocus,
            skinType = skinType,
            routineType = routineType,
            isOnboardingCompleted = true
        )
        userRepository.saveUserPreferences(preferences)
    }
}
