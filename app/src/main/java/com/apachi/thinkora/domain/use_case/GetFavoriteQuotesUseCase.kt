package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.model.Quote
import com.apachi.thinkora.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteQuotesUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    operator fun invoke(): Flow<List<Quote>> {
        return quoteRepository.getFavoriteQuotes()
    }
}
