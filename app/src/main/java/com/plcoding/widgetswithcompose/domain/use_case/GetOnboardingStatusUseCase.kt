package com.plcoding.widgetswithcompose.domain.use_case

import com.plcoding.widgetswithcompose.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOnboardingStatusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userRepository.getUserPreferences().map { it?.isOnboardingCompleted == true }
    }
}
