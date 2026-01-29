package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.model.DailyStreak
import com.apachi.thinkora.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyStreakUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    operator fun invoke(): Flow<DailyStreak> {
        return quoteRepository.getDailyStreak()
    }
}
