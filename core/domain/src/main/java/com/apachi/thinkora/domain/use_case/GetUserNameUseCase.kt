package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<String> {
        return userRepository.getUserPreferences().map { it?.userName ?: "" }
    }
}
