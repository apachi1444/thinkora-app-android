package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.Quote
import com.apachi.auraskin.domain.repository.QuoteRepository
import com.apachi.auraskin.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetDailyQuoteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Quote?> {
        return userRepository.getUserPreferences().flatMapLatest { preferences ->
            if (preferences == null) {
                flowOf(null)
            } else {
                quoteRepository.getDailyQuote(preferences.interestCategories)
            }
        }
    }
}
