package com.plcoding.widgetswithcompose.domain.use_case

import com.plcoding.widgetswithcompose.domain.model.UserPreferences
import com.plcoding.widgetswithcompose.domain.repository.UserRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, interests: List<String>) {
        val preferences = UserPreferences(
            userName = name,
            interestCategories = interests,
            isOnboardingCompleted = true
        )
        userRepository.saveUserPreferences(preferences)
    }
}
