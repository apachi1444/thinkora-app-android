package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.repository.HabitRepository
import com.apachi.thinkora.domain.repository.NotificationRepository
import com.apachi.thinkora.domain.repository.QuoteRepository
import com.apachi.thinkora.domain.repository.UserRepository
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
