package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.repository.HabitRepository
import com.apachi.auraskin.domain.repository.NotificationRepository
import com.apachi.auraskin.domain.repository.QuoteRepository
import com.apachi.auraskin.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val habitRepository: HabitRepository,
    private val quoteRepository: QuoteRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke() {
        habitRepository.deleteAllHabits()
        quoteRepository.deleteAllCustomQuotes()
        notificationRepository.deleteAllNotifications()
        userRepository.clearUserData()
    }
}
